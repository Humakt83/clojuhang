(ns clojuhang.game
  (:require [clojure.string :as string]))

(defn read-words []
  (vec (.split (slurp "resources/words.txt") " ")))

(defn pick-random-word []
  (let [words (read-words)]
  (.toUpperCase (get words (rand-int (.length words))))))

(defn show-word [word guessed-letters]
	(string/join " " (map (fn[x] (if (.contains guessed-letters (str x)) 
	                             (str x) 
	                             "_"))
	                     word)))

(defn letter-is-part-of-word [word guessed-letter]
  (.contains word guessed-letter))

(defn word-is-solved [word guessed-letters]
  (not (.contains (show-word word guessed-letters) "_")))

(defn progress-text [word hanged-state guessed-letters]
  (cond
    (>= hanged-state 6) "Game over"
    (word-is-solved word guessed-letters) "Victory"
    :else (format "%d guesses left" (- 6 hanged-state))
  ))

(defn start []
  (let [word (pick-random-word)
        guessedLetters (atom ())]
  (loop [n 0]
    (when (< n 6)
      (println "Guessed letters:" (str @guessedLetters))
      (println "Guess a letter")
      (let [guessedLetter (str (first (read-line)))]
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
  (println "Word was:" (.toUpperCase word))))

(show-word "word" ["w" "r"])