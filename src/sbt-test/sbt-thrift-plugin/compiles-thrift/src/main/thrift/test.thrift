namespace * com.intenthq.sbt.thrift

enum SomeEnum {
  VALUE_A,
  VALUE_B,
  VALUE_C
}

struct SomeStruct {
  1: required string fieldA;
  2: required SomeEnum fieldB;
  3: string fieldC;
}
