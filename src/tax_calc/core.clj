(ns tax-calc.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def nz-tax '(
  { :from 0 :to 14000 :tax 10.5 }
  { :from 14000 :to 48000 :tax 17.5 }
  { :from 48000 :to 70000 :tax 30 }
  { :from 70000 :tax 33 }))

(def sg-tax '(
  { :from 20000 :to 30000 :tax 2 }
  { :from 30000 :to 40000 :tax 3.5 }
  { :from 40000 :to 80000 :tax 7 }
  { :from 80000 :to 120000 :tax 11.5 }
  { :from 120000 :to 160000 :tax 15 }
  { :from 160000 :to 200000 :tax 18 }
  { :from 200000 :to 240000 :tax 19 }
  { :from 240000 :to 280000 :tax 19.5 }
  { :from 280000 :to 320000 :tax 20 }
  { :from 320000 :tax 22 }))

(defn country-parser [allowed]
  (fn [v] 
    (if (contains? allowed v) v "NZ")))

(def cli-options
  [["-c" "--country COUNTRY" "Country Code"
    :default "NZ"
    :parse-fn (country-parser #{"NZ" "SG"})]
   ["-i" "--income INCOME" "Income value"
    :default 0
    :parse-fn #(Integer/parseInt %)]])

(defn assert-bracket [b] 
  (assoc {} 
    :to (or (get b :to) ##Inf) 
    :from (or (get b :from) 0)
    :tax (or (get b :tax) 0)))

(defn tax-on-bracket [b income]
  (let [
    to (:to b) 
    from (:from b)]
    (float (* (/ (:tax b) 100) 
      (if (<= income from) 0 
      (if (<= income to) (- income from) 
      (- to from)))))))

(defn sum [f coll]
  (reduce (fn [acc val] (+ acc (f val))) 0 coll))

(defn tax-rates [code]
  (case code 
    "NZ" nz-tax
    "SG" sg-tax))

(defn tax-on-income [country income]
  (sum #(tax-on-bracket (assert-bracket %) income) (tax-rates country)))

(defn -main
  "Main calculator function"
  [& args]
  (let [opt (:options (parse-opts args cli-options))]
    (println (tax-on-income (:country opt) (:income opt)))))
