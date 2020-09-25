(ns clogit.utils
  (:require [clojure.java.io :as io])
  (:import java.util.Arrays
           java.security.MessageDigest))

(defn sha256
  "Credits: https://gist.github.com/kubek2k/8446062#file-sha256-clj"
  [string]
  (let [digest (.digest (MessageDigest/getInstance "SHA-256") (.getBytes string "UTF-8"))]
    (apply str (map (partial format "%02x") digest))))

(defn str->bytes [s]
  (map byte s))

(defn key->bytes [s]
  (-> s name str->bytes))

(defn path->bytes [path]
  (with-open [in  (io/input-stream path)
              out (java.io.ByteArrayOutputStream.)]
    (io/copy in out)
    (.toByteArray out)))

(defn split-bytes-by [sep bytes]
  (let [[fst _ rst] (partition-by sep bytes)]
    [(byte-array fst)
     (byte-array rst)]))
