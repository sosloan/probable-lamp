(ns data-is-code.homoiconicity
  "Demonstrates homoiconicity: the property that code and data share
   the same representation in Clojure (and Lisps in general).

   In Clojure:
   - Source code is written as data structures (lists, vectors, maps).
   - The reader parses text into data structures.
   - The compiler evaluates those data structures as code.
   - You can quote code to treat it as plain data.
   - You can eval data to execute it as code.")

;; ---------------------------------------------------------------------------
;; Section 1: Code as Data
;; ---------------------------------------------------------------------------

(def addition-as-data
  "A Clojure expression stored as a plain list (data, not evaluated)."
  '(+ 1 2 3))

(defn code-is-data-demo []
  (println "── Section 1: Code as Data ──────────────────────────────")
  (println "  A quoted expression is just a list (data):")
  (println "    '(+ 1 2 3) =>" addition-as-data)
  (println "    type       =>" (type addition-as-data))
  (println "    first      =>" (first addition-as-data))
  (println "    rest       =>" (rest addition-as-data))
  (println "  We can build expressions as data structures:")
  (let [expr (list '* 6 7)]
    (println "    (list '* 6 7) =>" expr)
    (println "    eval'd result =>" (eval expr))))

;; ---------------------------------------------------------------------------
;; Section 2: Data as Code (eval)
;; ---------------------------------------------------------------------------

(defn data-is-code-demo []
  (println "── Section 2: Data as Code (eval) ───────────────────────")
  (let [operations [['+ 10 20]
                    ['- 100 37]
                    ['* 6 7]
                    ['/ 84 2]]]
    (println "  Evaluating data structures as code:")
    (doseq [op operations]
      (let [expr (apply list op)
            result (eval expr)]
        (println (format "    (eval '%s) => %s" expr result))))))

;; ---------------------------------------------------------------------------
;; Section 3: Manipulating Code as Data
;; ---------------------------------------------------------------------------

(defn transform-expr
  "Takes a quoted arithmetic expression and doubles every numeric argument."
  [expr]
  (let [[op & args] expr]
    (apply list op (map #(if (number? %) (* 2 %) %) args))))

(defn manipulating-code-demo []
  (println "── Section 3: Manipulating Code as Data ─────────────────")
  (let [original '(+ 3 4 5)
        transformed (transform-expr original)]
    (println "  Original expression (data):  " original)
    (println "  After doubling numbers:      " transformed)
    (println "  Eval original:               " (eval original))
    (println "  Eval transformed:            " (eval transformed))))

;; ---------------------------------------------------------------------------
;; Section 4: Walking and Transforming Code Trees
;; ---------------------------------------------------------------------------

(defn walk-expr
  "Recursively walks a nested code tree, collecting all numbers."
  [expr]
  (cond
    (number? expr) [expr]
    (seq? expr)    (mapcat walk-expr (rest expr))
    :else          []))

(defn nested-code-demo []
  (println "── Section 4: Walking Code Trees ────────────────────────")
  (let [code '(+ (* 2 3) (- 10 (* 4 (+ 1 2))))
        numbers (walk-expr code)]
    (println "  Expression:" code)
    (println "  All numbers found in tree:" numbers)
    (println "  Evaluated result:" (eval code))))

;; ---------------------------------------------------------------------------
;; Public entry point
;; ---------------------------------------------------------------------------

(defn run-demos []
  (println "══ Homoiconicity Demos ══════════════════════════════════")
  (code-is-data-demo)
  (println)
  (data-is-code-demo)
  (println)
  (manipulating-code-demo)
  (println)
  (nested-code-demo))
