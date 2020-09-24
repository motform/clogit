(ns clogit.core
  (:require [clojure.tools.cli :as cli]
            [clogit.data :as data])
  (:gen-class))

(def cli-opts
  [["-h" "--help"]])

(defn valid-arg? [arg]
  (#{"init"} arg))

(defn init [args]
  (data/init))

(defn validate-args [args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args cli-opts :in-order true)]
    (cond
      (:help options) {:exit-msg summary :ok? true}
      errors {:exit-msg errors}
      (valid-arg? (first arguments)) {:action (first arguments) :options options}
      :else {:exit-msg summary})))

(defn exit [status msg]
  (println msg)
  ;; (System/exit status)
  (println "exit with status " status)) 

(defn -main [& args]
  (let [{:keys [action options exit-msg ok?]} (validate-args args)]
    (if exit-msg
      (exit (if ok? 0 1) exit-msg)
      (case action
        "init" (init options)
        (init options)))))

(comment
  (cli/parse-opts ["foo"] cli-opts :in-order true)
  (-main)
  (-main "init")
  (-main "foo")
  )
