(defproject data-is-code "0.1.0-SNAPSHOT"
  :description "A Clojure application demonstrating homoiconicity: data is code is data"
  :url "https://github.com/sosloan/probable-lamp"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.2"]]
  :main ^:skip-aot data-is-code.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
