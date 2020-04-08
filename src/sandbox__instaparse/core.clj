(ns sandbox--instaparse.core
  (:require [instaparse.core :as insta] [clojure.string :as s])
  (:gen-class))

(def dialogflow-log
  (insta/parser "dialogflow-log.bnf"))

(defn from-dialogflow [text]
  (->> text
       (dialogflow-log)
       (insta/transform {
         :S identity
         :STRING str
         :NUMBER read-string
         :SYMBOL str
         :PAIRS hash-map
       })))

(defn -main [& args]
  (-> args (first) (slurp) (s/trim) (from-dialogflow) (println)))
