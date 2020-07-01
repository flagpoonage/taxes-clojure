(defproject tax-calc "0.1.0-SNAPSHOT"
  :description "Tax calculator for NZ and SG"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [
    [org.clojure/clojure "1.10.1"]
    [org.clojure/tools.cli "1.0.194"]]
  :main ^:skip-aot tax-calc.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
