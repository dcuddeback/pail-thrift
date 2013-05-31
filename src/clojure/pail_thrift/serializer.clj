(ns pail-thrift.serializer
  "Defines a Pail serializer for Thrift objects."
  (:require [clj-thrift.serialization :as thrift]
            [clj-pail.serializer :as pail]))


;; ## ThriftSerializer

(defrecord ^{:doc "A Pail serializer for Thrift objects. It requires a type, a `TSerializer`, and a
                  `TDeserializer`. The serializer and deserializer should be configured to use the
                  same protocol, or else deserialization will not work."}
  ThriftSerializer
  [type serializer deserializer]

  pail/Serializer
  (pail/serialize [this object]
    (thrift/serialize (:serializer this) object))

  (pail/deserialize [this buffer]
    (thrift/deserialize (:deserializer this) (:type this) buffer)))


(defn thrift-serializer
  "Returns a `ThriftSerializer` configured to serialize objects of class `type`. It takes an
  optional `protocol` parameter which customizes the serialization protocol. `protocol` should be
  one of the protocols defined in `clj-thrift.protocol.factory`. If not specified, the default
  protocol will be used.

  `thrift-serializer` enforces the invariant of using the same protocol for the serializer and
  deserializer in a `ThriftSerializer`.

    ; defines a serializer for User objects
    (thrift-serializer com.example.thrift.User)

    ; defines a serializer using the compact protocol
    (thrift-serializer com.example.thrift.User
                       (protocol/compact))"
  ([type]
   (ThriftSerializer. type
                      (thrift/serializer)
                      (thrift/deserializer)))
  ([type protocol]
   (ThriftSerializer. type
                      (thrift/serializer protocol)
                      (thrift/deserializer protocol))))
