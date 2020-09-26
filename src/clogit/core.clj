(ns clogit.core
  "Primarily the functions governing the CLI."
  (:require [clojure.tools.cli :as cli]
            [clogit.base :as base]
            [clogit.data :as data])
  (:gen-class))

(defn init [args]
  (data/init))

(defn hash-object [{:keys [arguments]}]
  (-> arguments first slurp data/hash-object! println)) ; we assume there is only one file in the "arguments" seq

(defn cat-file [{:keys [arguments]}]
  (-> arguments first (data/oid->object nil) println))

(defn write-tree [args]
  (println (base/write-tree!)))

(def actions
  {"init" init
   "hash-object" hash-object
   "cat-file" cat-file
   "write-tree" write-tree})

(def cli-opts
  [["-h" "--help"]])

(defn- valid-arg? [arg] (actions arg))

(defn- validate-args [args]
  (let [{:keys [options arguments errors summary] :as args} (cli/parse-opts args cli-opts :in-order true)]
    (cond
      (:help options) {:exit-msg summary :ok? true}
      errors {:exit-msg errors}
      (valid-arg? (first arguments))
      (-> args (assoc :action (first arguments)) (update :arguments rest))
      :else {:exit-msg summary})))

(defn- exit [status msg]
  (println msg)
  ;; (System/exit status) ;; TODO add repl? predicate
  (println "exit with status " status)) 

(defn -main [& args]
  (let [{:keys [action exit-msg ok?] :as args} (validate-args args)]
    (if exit-msg
      (exit (if ok? 0 1) exit-msg)
      ((actions action) args))))

(comment
  ;;; Reset .ugit
  (do (doseq [f (reverse (file-seq (clojure.java.io/file ".ugit")))]
        (clojure.java.io/delete-file f))
      (-main "init"))

  ;;; Part 8
  (-main "write-tree")

  ;;; Part 4->5
  (spit "bla" "foo bar\n")
  (-main "hash-object" "bla")
  (-main "cat-file" "2fca24a0e1432ead5065dc759fbc33cf4b7934e055c9f1dd6a8f158d05143dae")

  ;;; Part 1->3
  (cli/parse-opts ["foo"] cli-opts :in-order true)
  (validate-args ["hash-object" "foo/bar"])
  (-main))
