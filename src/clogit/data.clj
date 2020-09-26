(ns clogit.data
  "Data/object/file-related functions.

  I opted to use straight `java.io` calls instead of a nicer
  library like `byte-streams`, to cut down on dependencies
  and to finally learn how it works. Its not very pretty though.

  In cases where a fn take an optional argument, they are
  implemented with multi-arity."
  (:require [clojure.java.io :as io]
            [clogit.utils :as utils]))

(def git-dir ".ugit")
(def obj-dir (str git-dir "/objects/"))

(defn init []
  (.mkdir (io/file git-dir))
  (.mkdir (io/file obj-dir)))

(defn- tag-with [type obj]
  (let [tag (utils/key->bytes type)
        obj (utils/str->bytes obj)
        null (utils/str->bytes "\0")]
    (byte-array (concat tag null obj))))

(defn hash-object!
  ([data] (hash-object! data :blob))
  ([data type]
   (let [obj (tag-with type data)
         oid (utils/sha256 (String. obj))]
     (with-open [out (io/output-stream (str obj-dir oid))]
       (.write out obj))
     oid)))

(defn- expected-tag? [expected tag]
  (= (String. tag) (name expected)))

(defn oid->object
  ([oid] (oid->object oid :blob))
  ([oid expected]
   (let [obj (utils/path->bytes (str obj-dir oid))
         [tag data] (utils/split-bytes-by #(= % (byte 0x00)) obj)]
     (when expected
       (assert (expected-tag? expected tag)
               (str "Expected " (name expected) ", got " (String. tag))))
     data)))
