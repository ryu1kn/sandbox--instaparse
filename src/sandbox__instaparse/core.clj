(ns sandbox--instaparse.core
  (:require [instaparse.core :as insta] [clojure.string :as s])
  (:gen-class))

(def as-and-bs
  (insta/parser
    "S = AB*
     AB = A B
     A = 'a'+
     B = 'b'+
    "))

(def dialogflow-log
  (insta/parser "dialogflow-log.bnf"))

(defn -main [& args]
  (-> args (first) (slurp) (s/trim) (as-and-bs) (println)))
