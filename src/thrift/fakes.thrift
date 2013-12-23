namespace * pail_thrift.fakes

union Properties {
  1: i16 age;
  2: string phone;
  3: string city;
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
