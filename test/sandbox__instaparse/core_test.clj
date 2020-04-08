(ns sandbox--instaparse.core-test
  (:require [clojure.test :refer :all]
            [sandbox--instaparse.core :refer :all]
            [clojure.string :as string]))

(defn escaped-string [s] (into [] (concat [:STRING] (string/split s #""))))

(deftest dialogflow-log-string
  (testing "empty string"
    (is (= [:S [:STRING]] (dialogflow-log "\"\""))))

  (testing "string"
    (is (= [:S [:STRING "a"]] (dialogflow-log "\"a\""))))

  (testing "string"
    (is (= [:S [:STRING "a" "b"]] (dialogflow-log "\"ab\""))))

  (testing "string - alphabets"
    (is (=
      [:S (escaped-string "abcdefghijklmnopqrstuvwxuzABCDEFGHIJKLMNOPQRSTUVWXUZ")]
      (dialogflow-log "\"abcdefghijklmnopqrstuvwxuzABCDEFGHIJKLMNOPQRSTUVWXUZ\""))))

  (testing "string - digits"
    (is (= [:S (escaped-string "0123456789")] (dialogflow-log "\"0123456789\""))))

  (doseq [symbol "`~!@#$%^&*()-_=+|[]{};:',.<>/?"]
    (let [s (str symbol)]
      (testing (str "string - symbols (" s ")")
        (is (= [:S [:STRING s]] (dialogflow-log (str "\"" s "\"")))))))

  (testing "string - space ( )"
    (is (= [:S [:STRING " "]] (dialogflow-log "\" \""))))

  (testing "string - symbols (\\)"
    (is (= [:S [:STRING "\\"]] (dialogflow-log "\"\\\""))))

  (testing "string - symbols (\")"
    (is (= [:S [:STRING "\""]] (dialogflow-log "\"\\\"\"")))))

(deftest dialogflow-log-number
  (testing "number"
    (is (= [:S [:NUMBER "0"]] (dialogflow-log "0")))))
