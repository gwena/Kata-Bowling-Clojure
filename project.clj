(defproject bowling-kata "1.0.0"
  :description "The Bowling Kata"
  :url "http://github.com/gwena/Kata-Bowling-Clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :main bowling-kata.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
