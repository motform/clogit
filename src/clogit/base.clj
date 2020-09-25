(ns clogit.base
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clogit.data :as data]))

(defn ignored? [entry]
  (str/includes? entry ".ugit"))

(defn write-tree
  ([] (write-tree "."))
  ([dir]
   (doseq [entry (.list (io/file dir))]
     (let [path (str dir "/" entry)
           entry (io/file path)]
       (cond (ignored? entry) nil
             (.isFile entry) (println entry)
             (.isDirectory entry) (write-tree path))))))

