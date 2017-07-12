(ns clojuhang.core)

(defn readWords []
  (vec (.split (slurp "resources/words.txt") " ")))

(defn pickRandomWord []
  (def words (readWords))  
  (get words (rand-int (.length words))))

(defn start []
  (pickRandomWord))
(start)