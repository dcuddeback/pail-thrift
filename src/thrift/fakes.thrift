namespace * pail_thrift.fakes

struct Location {
  1: optional string address;
  2: optional string city;
  3: optional string country;
}

union PropertyValues {
  1: i16 age;
  2: string phone;
  4: Location location;
}

struct Properties {
  1: string name;
  2: PropertyValues property;
}

struct Name {
  1: required string firstName;
  2: required string lastName;
}

union Identity {
  1: Name name;
  2: string ssn;
  3: Properties property;
}
