(defproject pail-thrift "0.1.0"
  :description "Serialization and partitioning strategies for using Thrift with clj-pail."
  :url "https://github.com/dcuddeback/pail-thrift"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}

  :min-lein-version "2.0.0"

  :source-paths ["src/clojure"]

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-pail "0.1.3"]
                 [clj-thrift "0.1.0"]]

  :profiles {:1.3 {:dependencies [[org.clojure/clojure "1.3.0"]]}
             :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0-master-SNAPSHOT"]]}

             :dev {:dependencies [[midje "1.5.1"]]
                   :plugins [[lein-thriftc "0.1.0"]
                             [lein-midje "3.0.1"]]
                   :prep-tasks ["thriftc"]}

             :provided {:dependencies [[org.slf4j/slf4j-log4j12 "1.5.2"]]}}

  :repositories {"sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail :update :always}}
                 "sonatype-snapshots" {:url "http://oss.sonatype.org/content/repositories/snapshots"
                                       :snapshots true
                                       :releases {:checksum :fail :update :always}}}

  :deploy-repositories [["releases" {:url "https://clojars.org/repo" :username :gpg :password :gpg}]
                        ["snapshots" {:url "https://clojars.org/repo" :username :gpg :password :gpg}]])
