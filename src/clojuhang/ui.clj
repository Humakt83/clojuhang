(ns clojuhang.ui
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.diff :refer [component defui render should-update?]]
            [fn-fx.controls :as ui]))

(def red (ui/color :red 1 :blue 0 :green 0))
(def blue (ui/color :red 0 :blue 1 :green 0))

(defn material-for-state [hanged-state body-part]
  (ui/phong-material
    :diffuse-color (if (>= hanged-state body-part)
                     blue
                     red)))

(defmulti handle-event (fn [state {:keys [event]}]
                         event))

(defui MainWindow
       (render [this {:keys [hanged-state]}]
         (ui/group
           :children [(ui/sphere :radius 25
                                 :material (material-for-state hanged-state 1)
                                 :translate-x 300 :translate-y -140 :translate-z 600)
                      (ui/cylinder :radius 20 :height 75
                                   :material (material-for-state hanged-state 2)
                                   :translate-x 300 :translate-y -75 :translate-z 600)
                      (ui/cylinder :radius 10 :height 85
                                   :material (material-for-state hanged-state 3)
                                   :translate-x 250 :translate-y -75 :translate-z 600
                                   :rotate 45)
                      (ui/cylinder :radius 10 :height 85
                                   :material (material-for-state hanged-state 4)
                                   :translate-x 350 :translate-y -75 :translate-z 600
                                   :rotate 135)
                      (ui/cylinder :radius 13 :height 95
                                   :material (material-for-state hanged-state 5)
                                   :translate-x 265 :translate-y 0 :translate-z 600
                                   :rotate 40)
                      (ui/cylinder :radius 13 :height 95
                                   :material (material-for-state hanged-state 6)
                                   :translate-x 345 :translate-y 0 :translate-z 600
                                   :rotate 130)
                      (ui/point-light :translate-x 350 :translate-y 100 :translate-z 300)])))

(defui Stage
       (render [this args]
         (ui/stage
           :title "Clojuhang - Clojure Hangman"
           :shown true
           :scene (ui/scene
                    :width 400
                    :height 400
                    :depth-buffer true
                    :anti-aliasing javafx.scene.SceneAntialiasing/BALANCED
                    :camera (ui/perspective-camera
                              :fixed-eye-at-camera-zero false
                              :translate-x 100 :translate-y -150 :translate-z 300)
                    :root (main-window :hanged-state (:hanged-state args))))))

(defn -main []
  (let [;; Data State holds the business logic of our app
        data-state (atom {:hanged-state 3})
         ;; handler-fn handles events from the ui and updates the data state
        handler-fn (fn [event]
                     (try
                       (swap! data-state handle-event event)
                       (catch Throwable ex
                         (println ex))))

        ;; ui-state holds the most recent state of the ui
        ui-state   (agent (dom/app (stage @data-state) handler-fn))]

    ;; Every time the data-state changes, queue up an update of the UI
    (add-watch data-state :ui (fn [_ _ _ _]
                                (send ui-state
                                      (fn [old-ui]
                                        (try
                                          (dom/update-app old-ui (stage @data-state))
                                          (catch Throwable ex
                                            (println ex)))))))))

(-main)
