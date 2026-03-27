(ns data-is-code.core-test
  (:require [clojure.test :refer :all]
            [data-is-code.homoiconicity :as homo]
            [data-is-code.macros :as macros]
            [data-is-code.dsl :as dsl]))

;; ---------------------------------------------------------------------------
;; Homoiconicity tests
;; ---------------------------------------------------------------------------

(deftest code-as-data-test
  (testing "Quoted expressions are plain Clojure lists"
    (is (list? homo/addition-as-data))
    (is (= '+ (first homo/addition-as-data)))
    (is (= '(1 2 3) (rest homo/addition-as-data)))))

(deftest eval-data-as-code-test
  (testing "eval executes a data structure as code"
    (is (= 6  (eval '(+ 1 2 3))))
    (is (= 42 (eval '(* 6 7))))
    (is (= 42 (eval (list '* 6 7))))))

(deftest transform-expr-test
  (testing "transform-expr doubles all numeric arguments"
    (is (= '(+ 6 8 10) (homo/transform-expr '(+ 3 4 5))))
    (is (= '(* 12 14)  (homo/transform-expr '(* 6 7)))))
  (testing "operator symbol is preserved"
    (let [[op] (homo/transform-expr '(- 2 3))]
      (is (= '- op)))))

(deftest walk-expr-test
  (testing "walk-expr collects all numbers from a nested expression"
    (is (= [2 3 4 1 2] (homo/walk-expr '(+ (* 2 3) (- 4 (* 1 2))))))
    (is (= []           (homo/walk-expr '(foo bar))))
    (is (= [42]         (homo/walk-expr 42)))))

;; ---------------------------------------------------------------------------
;; Macro tests
;; ---------------------------------------------------------------------------

(deftest unless-macro-test
  (testing "unless executes body when condition is false"
    (let [ran? (atom false)]
      (macros/unless false (reset! ran? true))
      (is @ran?)))
  (testing "unless does NOT execute body when condition is true"
    (let [ran? (atom false)]
      (macros/unless true (reset! ran? true))
      (is (not @ran?)))))

(deftest pipeline-macro-test
  (testing "defpipeline-generated function threads input through steps"
    (is (= 90 (macros/transform-numbers [1 2 3 4 5])))
    (is (= 0  (macros/transform-numbers [2 4 6])))
    (is (= 10 (macros/transform-numbers [1])))))

;; ---------------------------------------------------------------------------
;; DSL / interpreter tests
;; ---------------------------------------------------------------------------

(deftest rules-engine-test
  (testing "apply-rules returns correct labels"
    (is (= "FizzBuzz" (dsl/apply-rules dsl/transformation-rules 15)))
    (is (= "FizzBuzz" (dsl/apply-rules dsl/transformation-rules 30)))
    (is (= "Fizz"     (dsl/apply-rules dsl/transformation-rules 3)))
    (is (= "Buzz"     (dsl/apply-rules dsl/transformation-rules 5)))
    (is (= "7"        (dsl/apply-rules dsl/transformation-rules 7)))))

(deftest interpret-test
  (testing "interpret evaluates literal numbers"
    (is (= 42 (dsl/interpret 42))))
  (testing "interpret evaluates :add"
    (is (= 10 (dsl/interpret {:op :add :args [3 7]}))))
  (testing "interpret evaluates :mul"
    (is (= 42 (dsl/interpret {:op :mul :args [6 7]}))))
  (testing "interpret evaluates :sub"
    (is (= 58 (dsl/interpret {:op :sub :args [100 42]}))))
  (testing "interpret evaluates :div"
    (is (= 6 (dsl/interpret {:op :div :args [42 7]}))))
  (testing "interpret evaluates nested expressions"
    (is (= 100 (dsl/interpret dsl/sample-program)))))

(deftest data->clojure-expr-test
  (testing "data->clojure-expr converts map tree to s-expression with Clojure operators"
    (is (= '(+ 3 4) (dsl/data->clojure-expr {:op :add :args [3 4]})))
    (is (= 42       (dsl/data->clojure-expr 42)))
    (let [expr (dsl/data->clojure-expr {:op :mul :args [6 7]})]
      (is (list? expr))
      (is (= '* (first expr))))))

