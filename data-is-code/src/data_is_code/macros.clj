(ns data-is-code.macros
  "Demonstrates macros: compile-time code transformation.

   Macros receive their arguments as unevaluated data (code),
   transform that data, and return new code to be compiled.
   This is the most direct embodiment of 'data is code is data':
   the macro system reads code as data, manipulates it as data,
   and emits new code (data) to be executed.")

;; ---------------------------------------------------------------------------
;; Section 1: A simple logging macro
;; ---------------------------------------------------------------------------

(defmacro log-expr
  "Prints both the source form and its evaluated value."
  [expr]
  `(let [result# ~expr]
     (println (format "  expr: %-30s => %s" '~expr result#))
     result#))

(defn logging-macro-demo []
  (println "── Section 1: Logging Macro ─────────────────────────────")
  (println "  The macro receives code as data and wraps it:")
  (log-expr (+ 1 2 3))
  (log-expr (* 6 7))
  (log-expr (str "hello" " " "world"))
  (log-expr (map inc [1 2 3])))

;; ---------------------------------------------------------------------------
;; Section 2: unless — a control-flow macro
;; ---------------------------------------------------------------------------

(defmacro unless
  "Evaluates body when condition is falsy (the opposite of when)."
  [condition & body]
  `(when (not ~condition)
     ~@body))

(defn unless-macro-demo []
  (println "── Section 2: unless Macro ──────────────────────────────")
  (println "  Macro expansion of (unless false (println \"runs!\")):")
  (println " " (macroexpand-1 '(unless false (println "runs!"))))
  (print "  Output: ")
  (unless false (println "unless ran because condition was false"))
  (unless true  (println "this should NOT print")))

;; ---------------------------------------------------------------------------
;; Section 3: while — looping macro built from code transformation
;; ---------------------------------------------------------------------------

(defmacro my-while
  "Executes body repeatedly while condition is true."
  [condition & body]
  `(loop []
     (when ~condition
       ~@body
       (recur))))

(defn while-macro-demo []
  (println "── Section 3: my-while Macro ────────────────────────────")
  (println "  Expansion:" (macroexpand-1 '(my-while (< x 3) (println x) (inc x))))
  (let [counter (atom 0)]
    (print "  Counting: ")
    (my-while (< @counter 5)
      (print @counter " ")
      (swap! counter inc))
    (println)))

;; ---------------------------------------------------------------------------
;; Section 4: defpipeline — a data-driven pipeline macro
;; ---------------------------------------------------------------------------

(defmacro defpipeline
  "Defines a named function that threads a collection through a series of steps
   using the thread-last (->>)  operator, so each step receives the collection
   as its last argument — which is the correct convention for filter/map/reduce.

   Example:
     (defpipeline process-numbers
       (filter odd?)
       (map #(* % 10))
       (reduce +))

     (process-numbers [1 2 3 4 5]) => 90"
  [name & steps]
  `(defn ~name [input#]
     (->> input# ~@steps)))

(defpipeline transform-numbers
  (filter odd?)
  (map #(* % 10))
  (reduce +))

(defn pipeline-macro-demo []
  (println "── Section 4: defpipeline Macro ─────────────────────────")
  (println "  The macro generates a function from declarative data:")
  (println "  (transform-numbers [1 2 3 4 5])")
  (println "  => filter odd? => [1 3 5]")
  (println "  => map (* 10) => [10 30 50]")
  (println "  => reduce +  =>" (transform-numbers [1 2 3 4 5])))

;; ---------------------------------------------------------------------------
;; Public entry point
;; ---------------------------------------------------------------------------

(defn run-demos []
  (println "══ Macro Demos ══════════════════════════════════════════")
  (logging-macro-demo)
  (println)
  (unless-macro-demo)
  (println)
  (while-macro-demo)
  (println)
  (pipeline-macro-demo))
