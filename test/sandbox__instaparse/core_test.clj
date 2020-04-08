(ns sandbox--instaparse.core-test
  (:require [clojure.test :refer :all]
            [sandbox--instaparse.core :refer :all]
            [clojure.string :as string]))

(defn escaped-string [s] (into [] (concat [:STRING] (string/split s #""))))

(deftest dialogflow-log-string
  (testing "string - empty"
    (is (= "" (from-dialogflow "\"\""))))

  (testing "string"
    (is (= "a" (from-dialogflow "\"a\""))))

  (testing "string - multiple characters"
    (is (= "ab" (from-dialogflow "\"ab\""))))

  (testing "string - alphabets"
    (is (=
      "abcdefghijklmnopqrstuvwxuzABCDEFGHIJKLMNOPQRSTUVWXUZ"
      (from-dialogflow "\"abcdefghijklmnopqrstuvwxuzABCDEFGHIJKLMNOPQRSTUVWXUZ\""))))

  (testing "string - digits"
    (is (= "0123456789" (from-dialogflow "\"0123456789\""))))

  (doseq [symbol "`~!@#$%^&*()-_=+|[]{};:',.<>/?"]
    (let [s (str symbol)]
      (testing (str "string - symbols (" s ")")
        (is (= s (from-dialogflow (str "\"" s "\"")))))))

  (testing "string - space ( )"
    (is (= " " (from-dialogflow "\" \""))))

  (testing "string - symbols (\\)"
    (is (= "\\" (from-dialogflow "\"\\\""))))

  (testing "string - symbols (\")"
    (is (= "\"" (from-dialogflow "\"\\\"\"")))))

(deftest dialogflow-log-number
  (testing "number - single digit"
    (is (= 0 (from-dialogflow "0"))))

  (testing "number - multiple digits 1-5"
    (is (= 12345 (from-dialogflow "12345"))))

  (testing "number - multiple digits 5-0"
    (is (= 567890 (from-dialogflow "567890"))))

  (testing "number - float point"
    (is (= 0.9 (from-dialogflow "0.9"))))

  (testing "number - float point"
    (is (= 0.0 (from-dialogflow "0.0"))))
  )

(deftest dialogflow-log-object
  (testing "object - empty"
    (is (= {} (from-dialogflow "{}"))))

  (testing "object - single key"
    (is (= {"a" "b"} (from-dialogflow "{a:\"b\"}"))))

  (testing "object - multiple keys"
    (is (= {"a" "b" "c" "d"} (from-dialogflow "{a:\"b\"\nc:\"d\"}"))))

  (testing "object - object value"
    (is (= {"a" {}} (from-dialogflow "{a {}}"))))

  (testing "object - without braces"
    (is (= {"a" "b"} (from-dialogflow "a:\"b\""))))

  (testing "object - key format"
    (is (= {"abcdefghijklmnopqrstuvwxyz0123456789_" 3} (from-dialogflow "abcdefghijklmnopqrstuvwxyz0123456789_:3"))))
  )
