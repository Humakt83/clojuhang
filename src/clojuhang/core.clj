(ns clojuhang.core)

(defn readWords []
  (vec (.split (slurp "resources/words.txt") " ")))

(defn pickRandomWord []
  (def words (readWords))  
  (get words (rand-int (.length words))))

(defn start []
  (def word (pickRandomWord))
  (loop [n 0]
    (when (< n 6)
      (println "Guess a letter")
      (let [guessedLetter (read-line)]
        (if (.contains word guessedLetter)
          (println "Correct")
          (println "Wrong")))
      (recur (inc n))))
  (println "Word was:" (.toUpperCase word)))

(start)