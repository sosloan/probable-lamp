(ns data-is-code.dsl
  "Demonstrates data-driven programming and EDN as a DSL.

   Because Clojure data structures are the language itself, you can
   represent programs, configurations, and domain rules entirely as
   maps, vectors, and lists — then interpret or compile them at
   runtime.  This is the practical pay-off of 'data is code is data'."
  (:require [clojure.pprint :refer [pprint]]))

;; ---------------------------------------------------------------------------
;; Section 1: Data-driven dispatch (rules engine)
;; ---------------------------------------------------------------------------

(def transformation-rules
  "A pure-data description of number transformations (Fizz-Buzz style)."
  [{:divisor 15 :label "FizzBuzz"}
   {:divisor 3  :label "Fizz"}
   {:divisor 5  :label "Buzz"}])

(defn apply-rules
  "Applies the first matching rule from a data-defined rule set."
  [rules n]
  (or (some (fn [{:keys [divisor label]}]
              (when (zero? (mod n divisor)) label))
            rules)
      (str n)))

(defn rules-engine-demo []
  (println "── Section 1: Data-Driven Rules Engine ──────────────────")
  (println "  Rules (plain data):")
  (doseq [rule transformation-rules]
    (println "   " rule))
  (println "  Results for 1-20:")
  (println " " (map (partial apply-rules transformation-rules) (range 1 21))))

;; ---------------------------------------------------------------------------
;; Section 2: Interpreter for a tiny arithmetic DSL
;; ---------------------------------------------------------------------------

(defn interpret
  "Interprets a nested map-based arithmetic expression tree.

   Expression format:
     {:op :add  :args [expr1 expr2]}
     {:op :mul  :args [expr1 expr2]}
     {:op :sub  :args [expr1 expr2]}
     {:op :div  :args [expr1 expr2]}
     number (literal)"
  [expr]
  (if (number? expr)
    expr
    (let [{:keys [op args]} expr
          evaluated (map interpret args)]
      (case op
        :add (apply + evaluated)
        :sub (apply - evaluated)
        :mul (apply * evaluated)
        :div (apply / evaluated)
        (throw (ex-info "Unknown op" {:op op}))))))

(def sample-program
  "A program encoded entirely as Clojure data (a nested map tree)."
  {:op   :add
   :args [{:op :mul :args [6 7]}
          {:op :sub :args [100 {:op :div :args [84 2]}]}]})

(defn interpreter-demo []
  (println "── Section 2: Data-as-Program Interpreter ───────────────")
  (println "  Program (pure data / map tree):")
  (pprint sample-program)
  (println "  Interpreted result:" (interpret sample-program))
  (println "  Equivalent Clojure: (+ (* 6 7) (- 100 (/ 84 2))) =>"
           (+ (* 6 7) (- 100 (/ 84 2)))))

;; ---------------------------------------------------------------------------
;; Section 3: Generating and evaluating code from data
;; ---------------------------------------------------------------------------

(def op->symbol
  "Maps DSL operation keywords to their Clojure operator symbols."
  {:add '+
   :sub '-
   :mul '*
   :div '/})

(defn data->clojure-expr
  "Converts a map-tree program (as in Section 2) into a Clojure s-expression
   using standard Clojure arithmetic operators."
  [expr]
  (if (number? expr)
    expr
    (let [{:keys [op args]} expr
          op-sym (get op->symbol op (symbol (name op)))]
      (apply list op-sym (map data->clojure-expr args)))))

(defn code-generation-demo []
  (println "── Section 3: Data → Code Generation ───────────────────")
  (let [generated (data->clojure-expr sample-program)]
    (println "  Data map  :" sample-program)
    (println "  Generated :" generated)
    (println "  Evaluated :" (eval generated))))

;; ---------------------------------------------------------------------------
;; Section 4: Configuration-driven function composition
;; ---------------------------------------------------------------------------

(def pipeline-config
  "A declarative data description of a data-processing pipeline."
  {:steps [{:fn :inc   :desc "increment each number"}
           {:fn :even? :desc "keep only even numbers (filter step)"}
           {:fn :str   :desc "convert to string"}]
   :input (range 1 11)})

(def step-registry
  "Maps keyword names to actual functions."
  {:inc  inc
   :even? even?
   :str  str})

(defn run-pipeline
  "Executes a declaratively-described pipeline against input data."
  [{:keys [steps input]}]
  (reduce (fn [data {:keys [fn desc]}]
            (let [f (get step-registry fn)]
              (println "    Step:" desc)
              (cond
                (= fn :even?) (filter f data)
                :else         (map f data))))
          input
          steps))

(defn config-pipeline-demo []
  (println "── Section 4: Config-Driven Pipeline ────────────────────")
  (println "  Pipeline config (data):")
  (pprint pipeline-config)
  (println "  Running pipeline:")
  (let [result (run-pipeline pipeline-config)]
    (println "  Result:" (vec result))))

;; ---------------------------------------------------------------------------
;; Public entry point
;; ---------------------------------------------------------------------------

(defn run-demos []
  (println "══ DSL / Data-Driven Demos ══════════════════════════════")
  (rules-engine-demo)
  (println)
  (interpreter-demo)
  (println)
  (code-generation-demo)
  (println)
  (config-pipeline-demo))
