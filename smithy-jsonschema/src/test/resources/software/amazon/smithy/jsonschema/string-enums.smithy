$version: "2.0"

namespace smithy.example

structure Foo {
    bar: TestEnum
}

@documentation("This is a test enum")
enum TestEnum {
    @title("Foo Title")
    @documentation("it really does foo")
    FOO = "Foo"
    @title("Bar Title")
    BAR = "Bar"
}
