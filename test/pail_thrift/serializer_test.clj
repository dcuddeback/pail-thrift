(ns pail-thrift.serializer-test
  (:require [pail-thrift.serializer :as s]
            [clj-pail.serializer :as pail]
            [clj-thrift.serialization :as thrift]
            [clj-thrift.protocol.factory :as protocol])
  (:import (pail_thrift.fakes Name Identity))
  (:use midje.sweet))


(facts "ThriftSerializer"

  (facts "serialize"

    (tabular "serializes with provided protocol"
      (let [object ?object
            protocol ?protocol
            pail-serializer (s/thrift-serializer (type object) protocol)
            thrift-serializer (thrift/serializer protocol)]
        (seq (pail/serialize pail-serializer object)) => (seq (thrift/serialize thrift-serializer object)))

      ?object                                 ?protocol
      ;struct
      (Name. "John" "Doe")                    (protocol/binary)
      (Name. "Joe" "Example")                 (protocol/compact)

      ; union
      (Identity/name (Name. "Sally" "Smith")) (protocol/json)
      (Identity/ssn "555-55-5555")            (protocol/tuple))


    (tabular "serializes with default protocol"
      (let [object ?object
            pail-serializer (s/thrift-serializer (type object))
            thrift-serializer (thrift/serializer)]
        (seq (pail/serialize pail-serializer object)) => (seq (thrift/serialize thrift-serializer object)))

      ?object
      ; struct
      (Name. "John" "Doe")
      (Name. "Joe" "Example")

      ; union
      (Identity/name (Name. "Sally" "Smith"))
      (Identity/ssn "555-55-5555")))


  (facts "deserialize"

    (tabular "deserializes with provided protocol"
      (let [object ?object
            protocol ?protocol
            pail-serializer (s/thrift-serializer (type object) protocol)
            thrift-serializer (thrift/serializer protocol)]

        (pail/deserialize pail-serializer (thrift/serialize thrift-serializer object)) => object)

      ?object                                 ?protocol
      ;struct
      (Name. "John" "Doe")                    (protocol/binary)
      (Name. "Joe" "Example")                 (protocol/compact)

      ; union
      (Identity/name (Name. "Sally" "Smith")) (protocol/json)
      (Identity/ssn "555-55-5555")            (protocol/tuple))

    (tabular "deserializes with default protocol"
      (let [object ?object
            pail-serializer (s/thrift-serializer (type object))
            thrift-serializer (thrift/serializer)]

        (pail/deserialize pail-serializer (thrift/serialize thrift-serializer object)) => object)

      ?object
      ;struct
      (Name. "John" "Doe")
      (Name. "Joe" "Example")

      ; union
      (Identity/name (Name. "Sally" "Smith"))
      (Identity/ssn "555-55-5555"))))
