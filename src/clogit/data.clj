(ns clogit.data
  (:require [clojure.java.io :as io]))

(def git-dir ".ugit")
(def obj-dir (str git-dir "/objects/"))

(defn init []
  (.mkdir (io/file git-dir))
  (.mkdir (io/file obj-dir)))

(defn hash-object [obj]
  (let [oid (-> obj slurp hash)]
    (spit (str obj-dir oid) (slurp obj))
    oid))

(defn oid->object [oid]
  (slurp (str obj-dir oid)))
