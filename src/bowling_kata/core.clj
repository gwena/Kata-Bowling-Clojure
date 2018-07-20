(ns bowling-kata.core
  (:require [clojure.string :as str]))

(def re-standard-frame #"\| (\d \d|\d /|X) ")
(def re-last-frame #"\| (\d \d|\d / (\d|X)|X (\d \d|\d /|X (\d|X))) \|")
(def re-9-frames (re-pattern (str "^((" re-standard-frame "){9})")))
(def re-all-frames (re-pattern (str re-9-frames re-last-frame \$)))

(defn ->int [digit]
  (Integer/parseInt digit))

(defn spare->down-pins [first-throw]
  (str first-throw " " (- 10 (->int first-throw))))

(defn frame->throws [frame]
  (-> frame
      (str/replace #"(\d) \/" #(spare->down-pins (second %)))
      (str/replace #"X" "10")
      (str/split #" ")
      (->> (map ->int))))

(defn score-frame [down-pins frames]
  (if (= 10 down-pins)
    (apply + (take 3 (flatten frames)))
    down-pins))

(defn score-frames [all-frames]
  (loop [frames all-frames, acc 0]
    (let [[first-frame & remainder] frames
          down-pins (apply + first-frame)]
      (if (seq remainder)
        (recur remainder
               (+ acc (score-frame down-pins frames)))
        (+ acc down-pins)))))

(defn score-game [game]
  (let [matches (re-matches re-all-frames game)]
    (assert (some? matches) "Bowling game information format not respected!")
    (let [standard-frames (->> (second matches)
                               (re-seq re-standard-frame)
                               (map second))
          last-frame (nth matches 4)
          all-frames (map frame->throws
                          (concat standard-frames
                                  [last-frame]))]
      (score-frames all-frames))))

(defn score-game-file [filename]
  (-> filename
      slurp
      str/trim-newline
      score-game))

(defn -main [& args]
  (assert (= 1 (count args))
          "Take only one argument: filename with line to score")
  (println (score-game-file (first args))))
