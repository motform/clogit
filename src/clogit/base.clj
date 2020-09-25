(ns clogit.base
  (:require [clojure.java.io :as io]
            [clogit.data :as data]))

(defn write-tree
  ([] (write-tree "."))
  ([dir]
   (doseq [entry (.list (io/file dir))]
     (let [path (str dir "/" entry)
           entry (io/file path)]
       (if (.isFile entry)
         (println entry)
         (write-tree path))))))

