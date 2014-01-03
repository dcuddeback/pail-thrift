(ns pail-thrift.partitioner-test
  (:require [pail-thrift.partitioner :as p]
            [clj-pail.partitioner :as pail]
            [clj-thrift.base :as base]
            [clj-thrift.union :as union]
            [clj-thrift.type :as type])
  (:import (pail_thrift.fakes Identity Location Name Properties))
  (:use midje.sweet))


(facts "UnionPartitioner"
  (let [partitioner (p/union-partitioner ..type..)]

    (fact "make-partition returns the current field's ID"
      (pail/make-partition partitioner ..union..) => (just [..field-id..])
      (provided
        (union/current-field-id ..union..) => ..field-id..))

    (tabular "make a partition for the current structure"
           (fact
            (let [partitioner (p/union-partitioner Identity)]
              (pail/make-partition partitioner (base/build ?type ?attributes)) => ?result))

           ?type         ?attributes                ?result
           Identity     {:property
                         {:name "Eric" :property
                          {:phone "555-555-5555"}}} [3]

           Identity     {:property
                         {:name "Eric" :property
                          {:location
                           {:city "Asheville"}}}}   [3]

           Identity     {:name
                         {:firstName "Eric"
                          :lastName "Gebhart"}}     [1])

    (tabular "validate"
      (do
        (fact "is valid if dir is a valid field ID"
          (pail/validate partitioner ?dirs) => (just [?result irrelevant])
          (provided
            (type/field-ids ..type..) => ?valid))

        (fact "returns extra directories"
          (pail/validate partitioner ?dirs) => (just [irrelevant ?extra])
          (against-background
            (type/field-ids anything) => ?valid)))

      ?valid    ?dirs   ?result ?extra
      #{1}      ["1"]   true    empty?
      #{1}      ["2"]   false   empty?
      #{2}      ["2"]   true    empty?
      #{1}      ["X"]   false   empty?
      #{1 2 3}  ["0"]   false   empty?
      #{1 2 3}  ["1"]   true    empty?
      #{1 2 3}  ["2"]   true    empty?
      #{1 2 3}  ["3"]   true    empty?
      #{1 2 3}  ["4"]   false   empty?

      #{1}      ["1" "extra"]                   true    (just ["extra"])
      #{1}      ["1" "foo" "bar" "baz" "qux"]   true    (just ["foo" "bar" "baz" "qux"])
      #{1}      ["2" "extra"]                   false   (just ["extra"])
      #{1}      ["2" "foo" "bar" "baz" "qux"]   false   (just ["foo" "bar" "baz" "qux"])
      #{1}      ["X" "extra"]                   false   (just ["extra"])
      #{1}      ["X" "foo" "bar" "baz" "qux"]   false   (just ["foo" "bar" "baz" "qux"]))))

(facts "UnionPropertyPartitioner"
  (let [partitioner (p/union-property-partitioner ..type..)]

    #_(fact "make-partition returns the current field's ID"
      (pail/make-partition partitioner ..union..) => (just [..field-id..])
      (provided
        (union/current-field-id ..union..) => ..field-id..))

    (tabular "make a partition for the current structure"
           (fact
            (let [partitioner (p/union-property-partitioner Identity)]
              (pail/make-partition partitioner (base/build ?type ?attributes)) => ?result))

           ?type         ?attributes                ?result
           Identity     {:property
                         {:name "Eric" :property
                          {:phone "555-555-5555"}}} [3 2]

           Identity     {:property
                         {:name "Eric" :property
                          {:location
                           {:city "Asheville"}}}}   [3 4]

           Identity     {:name
                         {:firstName "Eric"
                          :lastName "Gebhart"}}     [1])

    (tabular "validate"
      (do
        (fact "is valid if dir is a valid field ID"
          (pail/validate partitioner ?dirs) => (just [?result irrelevant])
          (provided
            (type/field-ids ..type..) => ?valid))

        (fact "returns extra directories"
          (pail/validate partitioner ?dirs) => (just [irrelevant ?extra])
          (against-background
            (type/field-ids anything) => ?valid)))

      ?valid    ?dirs   ?result ?extra
      #{1}      ["1"]   true    empty?
      #{1}      ["2"]   false   empty?
      #{2}      ["2"]   true    empty?
      #{1}      ["X"]   false   empty?
      #{1 2 3}  ["0"]   false   empty?
      #{1 2 3}  ["1"]   true    empty?
      #{1 2 3}  ["2"]   true    empty?
      #{1 2 3}  ["3"]   true    empty?
      #{1 2 3}  ["4"]   false   empty?

      #{1}      ["1" "extra"]                   true    (just ["extra"])
      #{1}      ["1" "foo" "bar" "baz" "qux"]   true    (just ["foo" "bar" "baz" "qux"])
      #{1}      ["2" "extra"]                   false   (just ["extra"])
      #{1}      ["2" "foo" "bar" "baz" "qux"]   false   (just ["foo" "bar" "baz" "qux"])
      #{1}      ["X" "extra"]                   false   (just ["extra"])
      #{1}      ["X" "foo" "bar" "baz" "qux"]   false   (just ["foo" "bar" "baz" "qux"]))))

(facts "UnionNamePartitioner"
  (let [partitioner (p/union-name-partitioner ..type..)]

    (fact "make-partition returns the current field's Name"
      (pail/make-partition partitioner ..union..) => (just [..field-name..])
      (provided
        (union/current-field-name ..union..) => ..field-name..))

    (tabular "make a partition for the current structure"
           (fact
            (let [partitioner (p/union-name-partitioner Identity)]
              (pail/make-partition partitioner (base/build ?type ?attributes)) => ?result))

           ?type         ?attributes                ?result
           Identity     {:property
                         {:name "Eric" :property
                          {:phone "555-555-5555"}}} ["property"]

           Identity     {:property
                         {:name "Eric" :property
                          {:location
                           {:city "Asheville"}}}}   ["property"]

           Identity     {:name
                         {:firstName "Eric"
                          :lastName "Gebhart"}}     ["name"])

    (tabular "validate"
      (do
        (fact "is valid if dir is a valid field Name"
          (pail/validate partitioner ?dirs) => (just [?result irrelevant])
          (provided
            (type/field-names ..type..) => ?valid))

        (fact "returns extra directories"
          (pail/validate partitioner ?dirs) => (just [irrelevant ?extra])
          (against-background
            (type/field-names anything) => ?valid)))

      ?valid           ?dirs          ?result ?extra
      #{"firstName"}    ["firstName"]  true    empty?
      #{"firstName"}   ["lastName"]   false   empty?
      #{"lastName"}    ["lastName"]   true    empty?
      #{"firstName"}   ["X"]          false   empty?
      #{"firstName" "lastName" "property"}  ["0"]           false   empty?
      #{"firstName" "lastName" "property"}  ["firstName"]   true    empty?
      #{"firstName" "lastName" "property"}  ["lastName"]    true    empty?
      #{"firstName" "lastName" "property"}  ["property"]    true    empty?
      #{"firstName" "lastName" "property"}  ["4"]           false   empty?

      #{"firstName"}      ["firstName" "extra"]                  true    (just ["extra"])
      #{"firstName"}      ["firstName" "foo" "bar" "baz" "qux"]  true    (just ["foo" "bar" "baz" "qux"])
      #{"firstName"}      ["lastName" "extra"]                   false   (just ["extra"])
      #{"firstName"}      ["lastName" "foo" "bar" "baz" "qux"]   false   (just ["foo" "bar" "baz" "qux"])
      #{"firstName"}      ["X" "extra"]                          false   (just ["extra"])
      #{"firstName"}      ["X" "foo" "bar" "baz" "qux"]          false   (just ["foo" "bar" "baz" "qux"]))))


(facts "UnionNamePropertyPartitioner"
  (let [partitioner (p/union-name-property-partitioner ..type..)]

    #_(fact "make-partition returns the current field's Name"
      (pail/make-partition partitioner ..union..) => (just [..field-name..])
      (provided
        (union/current-field-name ..union..) => ..field-name..))

    (tabular "make a partition for the current structure"
           (fact
            (let [partitioner (p/union-name-property-partitioner Identity)]
              (pail/make-partition partitioner (base/build ?type ?attributes)) => ?result))

           ?type         ?attributes                ?result
           Identity     {:property
                         {:name "Eric" :property
                          {:phone "555-555-5555"}}} ["property" "phone"]

           Identity     {:property
                         {:name "Eric" :property
                          {:location
                           {:city "Asheville"}}}}   ["property" "location"]

           Identity     {:name
                         {:firstName "Eric"
                          :lastName "Gebhart"}}     ["name"])

    (tabular "validate"
      (do
        (fact "is valid if dir is a valid field Name"
          (pail/validate partitioner ?dirs) => (just [?result irrelevant])
          (provided
            (type/field-names ..type..) => ?valid))

        (fact "returns extra directories"
          (pail/validate partitioner ?dirs) => (just [irrelevant ?extra])
          (against-background
            (type/field-names anything) => ?valid)))

      ?valid           ?dirs          ?result ?extra
      #{"firstName"}   ["firstName"]  true    empty?
      #{"firstName"}   ["lastName"]   false   empty?
      #{"lastName"}    ["lastName"]   true    empty?
      #{"firstName"}   ["X"]          false   empty?
      #{"firstName" "lastName" "property"}  ["0"]           false   empty?
      #{"firstName" "lastName" "property"}  ["firstName"]   true    empty?
      #{"firstName" "lastName" "property"}  ["lastName"]    true    empty?
      #{"firstName" "lastName" "property"}  ["property"]    true    empty?
      #{"firstName" "lastName" "property"}  ["4"]           false   empty?

      #{"firstName"}      ["firstName" "extra"]                  true    (just ["extra"])
      #{"firstName"}      ["firstName" "foo" "bar" "baz" "qux"]  true    (just ["foo" "bar" "baz" "qux"])
      #{"firstName"}      ["lastName" "extra"]                   false   (just ["extra"])
      #{"firstName"}      ["lastName" "foo" "bar" "baz" "qux"]   false   (just ["foo" "bar" "baz" "qux"])
      #{"firstName"}      ["X" "extra"]                          false   (just ["extra"])
      #{"firstName"}      ["X" "foo" "bar" "baz" "qux"]          false   (just ["foo" "bar" "baz" "qux"]))))
