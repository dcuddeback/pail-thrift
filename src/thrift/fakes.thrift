namespace * pail_thrift.fakes

struct Location {
  1: optional string address;
  2: optional string city;
  3: optional string country;
}

union Properties {
  1: i16 age;
  2: string phone;
  3: string city;
  4: Location location;
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
