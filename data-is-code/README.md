# data-is-code

A Clojure application that demonstrates **homoiconicity** — the property that
*data is code is data*.  In Clojure (and Lisp in general), source code is written
using the same data structures (lists, vectors, maps, symbols) that the language
provides as first-class values.  This means you can:

* **Quote** code to hold it as a plain data value.
* **Eval** a data structure to execute it as code.
* **Transform** code programmatically using ordinary Clojure functions.
* **Write macros** that receive code as data, manipulate it, and return new code.
* **Build DSLs** where domain rules are expressed as data and interpreted at
  runtime — no code generation step required.

## Project structure

```
src/data_is_code/
├── core.clj           – entry point, wires together all demos
├── homoiconicity.clj  – quote, eval, code manipulation, tree walking
├── macros.clj         – log-expr, unless, my-while, defpipeline macros
└── dsl.clj            – rules engine, interpreter, code generation, config pipeline

test/data_is_code/
└── core_test.clj      – unit tests for every module
```

## Prerequisites

* Java 11+
* [Clojure CLI](https://clojure.org/guides/install_clojure) **or** [Leiningen](https://leiningen.org/)

## Usage

### Run with Clojure CLI

```bash
clojure -M:run
```

### Run with Leiningen

```bash
lein run
```

### Build an uberjar

```bash
lein uberjar
java -jar target/uberjar/data-is-code-0.1.0-SNAPSHOT-standalone.jar
```

## Running tests

### Clojure CLI

```bash
clojure -X:test
```

### Leiningen

```bash
lein test
```

## Demo sections

| Section | Concept |
|---------|---------|
| **Homoiconicity 1** | Quoted expressions are plain `PersistentList` values |
| **Homoiconicity 2** | `eval` executes any list as Clojure code |
| **Homoiconicity 3** | Functions manipulate code trees the same way they manipulate data |
| **Homoiconicity 4** | Walking nested code trees with ordinary sequence functions |
| **Macros 1** | `log-expr` — a macro that wraps any expression for debug logging |
| **Macros 2** | `unless` — a control-flow macro, shown with `macroexpand-1` |
| **Macros 3** | `my-while` — a looping macro that compiles to `loop`/`recur` |
| **Macros 4** | `defpipeline` — generates a function from a declarative step list |
| **DSL 1** | Data-driven FizzBuzz rules engine |
| **DSL 2** | Interpreter for a map-tree arithmetic AST |
| **DSL 3** | Converting a data AST into a real Clojure s-expression and eval'ing it |
| **DSL 4** | Configuration-driven pipeline that composes functions from a data description |

## License

Copyright © 2026

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
https://www.eclipse.org/legal/epl-2.0.
