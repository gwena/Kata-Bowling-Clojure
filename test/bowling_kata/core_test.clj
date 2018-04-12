(ns bowling-kata.core-test
  (:require [clojure.test :refer :all]
            [bowling-kata.core :refer :all]))

(def kata-example-game "| 1 4 | 4 5 | 6 / | 5 / | X | 0 1 | 7 / | 6 / | X | 2 / 6 |")
(def perfect-game "| X | X | X | X | X | X | X | X | X | X X X |")
(def max-spare-game "| 9 / | 9 / | 9 / | 9 / | 9 / | 9 / | 9 / | 9 / | 9 / | 9 / 9 |")
(def worst-game "| 0 0 | 0 0 | 0 0 | 0 0 | 0 0 | 0 0 | 0 0 | 0 0 | 0 0 | 0 0 |")

(deftest valid-game-format-test
  (testing "with Kata example given"
    (is (some? (re-matches re-all-frames kata-example-game))))

  (testing "with perfect game"
    (is (some? (re-matches re-all-frames perfect-game))))

  (testing "with max using spares"
    (is (some? (re-matches re-all-frames max-spare-game))))

  (testing "with worst game"
    (is (some? (re-matches re-all-frames worst-game)))))

(deftest invalid-game-format-test
  (testing "with missing frames"
    (is (nil? (re-matches re-all-frames
                          "| 6 / | 5 / | X | 0 1 | 7 / | 6 / | X | 2 / 6 |"))))

  (testing "with starting spare in throw"
    (is (nil? (re-matches re-all-frames
                          "| / 2 | 4 5 | 6 / | 5 / | X | 0 1 | 7 / | 6 / | X | 2 / 6 |"))))

  (testing "with missing starting |"
    (is (nil? (re-matches re-all-frames
                          "| 6 / | 4 5 | 6 / | 5 / | X | 0 1 | 7 / | 6 / | X | 2 / 6 |GARBAGE"))))

  (testing "with extra information at the end"
    (is (nil? (re-matches re-all-frames
                          " 6 / | 4 5 | 6 / | 5 / | X | 0 1 | 7 / | 6 / | X | 2 / 6 |"))))

  (testing "with missing space between a | and digit"
    (is (nil? (re-matches re-all-frames
                          "| 6 / | 4 5 | 6 / |5 / | X | 0 1 | 7 / | 6 / | X | 2 / 6 |"))))

  (testing "with unrecognised character Y"
    (is (nil? (re-matches re-all-frames
                          "| 6 / | 4 5 | 6 / | 5 / | Y | 0 1 | 7 / | 6 / | X | 2 / 6 |")))))

(deftest frame->throws-test
  (testing "first frames"
    (testing "with a standard frame"
      (is (= [3 4] (frame->throws "3 4"))))

    (testing "with a spare"
      (is (= [8 2] (frame->throws "8 /"))))

    (testing "with a strike"
      (is (= [10] (frame->throws "X")))))

  (testing "last frame"
    (testing "with a spare then strike"
      (is (= [8 2 10] (frame->throws "8 / X"))))

    (testing "with a spare then standard"
      (is (= [8 2 2] (frame->throws "8 / 2"))))

    (testing "with a strike then standard"
      (is (= [10 1 2] (frame->throws "X 1 2"))))

    (testing "with a strike then a spare"
      (is (= [10 7 3] (frame->throws "X 7 /"))))

    (testing "with 2 strikes"
      (is (= [10 10 2] (frame->throws "X X 2"))))

    (testing "with 3 strikes"
      (is (= [10 10 10] (frame->throws "X X X"))))))

(deftest score-test
  (testing "single frame and last frame"
    (testing "with a standard frame"
      (is (= 10 (score-frames '((3 4) (1 2))))))

    (testing "with a spare"
      (is (= 14 (score-frames '((3 7) (1 2))))))

    (testing "with a strike"
      (is (= 16 (score-frames '((10) (1 2))))))))

(deftest score-game-test
  (testing "with Kata example (mix of everything)"
    (is (= 133 (score-game kata-example-game))))

  (testing "with perfect game"
    (is (= 300 (score-game perfect-game))))

  (testing "with max spares"
    (is (= 190 (score-game max-spare-game))))

  (testing "with worst game"
    (is (= 0 (score-game worst-game)))))

(deftest score-game-file-test
  (testing "with Kata example given"
    (is (= 133 (score-game-file "resources/input-game-test.txt")))))
