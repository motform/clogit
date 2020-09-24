(ns clogit.data
  (:require [clojure.java.io :as io]))

(def git-dir ".ugit")

(defn init []
  (.mkdir (io/file git-dir)))
