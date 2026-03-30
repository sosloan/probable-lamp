(ns data-is-code.core
  (:require [data-is-code.homoiconicity :as homo]
            [data-is-code.macros :as macros]
            [data-is-code.dsl :as dsl])
  (:gen-class))

(defn -main
  "Entry point for the data-is-code demonstration application.
   Runs all sections showing that data is code is data in Clojure."
  [& _args]
  (println "╔══════════════════════════════════════════════════════╗")
  (println "║        Data is Code is Data — Clojure Demo           ║")
  (println "╚══════════════════════════════════════════════════════╝")
  (println)
  (homo/run-demos)
  (println)
  (macros/run-demos)
  (println)
  (dsl/run-demos))
