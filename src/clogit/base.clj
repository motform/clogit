(ns clogit.base
  "Higher level functionality."
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clogit.data :as data]))

(declare write-tree!)

(defn- ignored? [entry]
  (or
   (str/includes? entry ".nrepl-port")
   (str/includes? entry ".cpcache")
   (str/includes? entry ".ugit")
   (str/includes? entry ".git")))

(defn- write-entries! [entries dir]
  (reduce (fn [entries entry]
            (let [path (str dir "/" entry)
                  entry (io/file path)
                  name (.getName entry)]
              (conj entries
                    (cond (ignored? name) nil
                          (.isFile entry)      [name (data/hash-object! (slurp path)) "blob"]
                          (.isDirectory entry) [name (write-tree! path) "tree"]))))
          [] entries))

(defn- entries->tree-obj [entries]
  (->> entries sort (map #(str/join " " %)) (str/join "\n")))

(defn write-tree!
  ([] (write-tree! "."))
  ([dir]
   (-> dir io/file .list
       (write-entries! dir)
       entries->tree-obj
       (data/hash-object! :tree))))
