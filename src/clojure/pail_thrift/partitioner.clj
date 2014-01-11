(ns pail-thrift.partitioner
  "Defines a Pail partitioner for Thrift unions."
  (:require [clj-pail.partitioner :as p]
            [clj-thrift.union :as union]
            [clj-thrift.type :as type]))


(defrecord ^{:doc "A pail partitioner for Thrift unions. It requires a type, which must be a subtype
                  of `TUnion`. The partitioner will partition based on the union's set field so that
                  all union values with the same field will be placed in the same partition."}
  UnionPartitioner
  [type]

  p/VerticalPartitioner
  (p/make-partition
    [this object]
    (vector (union/current-field-id object)))

  (p/validate
    [this dirs]
    [(try
       (contains? (type/field-ids type)
                  (Integer/valueOf (first dirs)))
       (catch NumberFormatException e
         false))
     (rest dirs)]))



(defn union-partitioner
  "Returns a `UnionPartitioner`. `type` should be a subclass of `TUnion`. Note that it should be the
  class, and not an instance."
  [type]
  (UnionPartitioner. type))



(defrecord ^{:doc "A pail partitioner for Thrift unions which uses field names instead of field id's.
                  It requires a type, which must be a subtype of `TUnion`. The partitioner will partition
                  based on the union's set field name so that all union values with the same field will be
                  placed in the same partition.

                    Union DataUnit {
                        1: PersonProperty MyProperties;
                        2: string Things;
                    }

                  Partitioning DataUnit will result in \"/MyProperties\" and \"/Things\" as the partitions."}


  UnionNamePartitioner
  [type]


  p/VerticalPartitioner
  (p/make-partition
    [this object]
    (vector (union/current-field-name object)))


  (p/validate
    [this dirs]
    [(contains?
      (type/field-names type)
      (first dirs))
     (rest dirs)])
  )

(defn union-name-partitioner
  [type]
  (UnionNamePartitioner. type))
