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
    (>= hanged-state 6) (format "Game over. Correct word was: %s" word)
    (word-is-solved word guessed-letters) "Victory"
    :else (format "%d guesses left" (- 6 hanged-state))
  ))
