(ns clojuhang.core)

(defn readWords []
  (vec (.split (slurp "resources/words.txt") " ")))

(defn pickRandomWord []
  (def words (readWords))  
  (get words (rand-int (.length words))))

(defn start []
  (def word (pickRandomWord))
  (def guessedLetters (atom ()))
  (loop [n 0]
    (when (< n 6)
      (println "Guessed letters:" (str @guessedLetters))
      (println "Guess a letter")
      (let [guessedLetter (.toString (first (read-line)))]
        (if (.contains word guessedLetter)
          (println "Correct")
          (println "Wrong"))
        (swap! guessedLetters conj guessedLetter))
      (defn doesNotContain 
        [x]
        (not (.contains (str @guessedLetters) (str x))))
      (if (<= (count (filter doesNotContain (seq word))) 0)
        (println "Victory!")
        (recur (inc n)))))
  (println "Word was:" (.toUpperCase word)))

(start)