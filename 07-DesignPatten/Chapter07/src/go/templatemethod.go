package main

import "fmt"

type TypeBase struct {
    i interface{}
}

type TypeA struct {
    TypeBase
}

type TypeB struct {
    TypeBase
}

type Oper interface {
    Operation1()
    Operation2()
}

func NewTypeA() *TypeBase {
    typeA := new(TypeBase)
    typeA.i = new(TypeA)
    return typeA
}

func NewTypeB() *TypeBase {
    typeB := new(TypeBase)
    typeB.i = new(TypeB)
    return typeB
}

func (self *TypeBase) templateMethod() {
    self.i.(Oper).Operation1()
    self.i.(Oper).Operation2()
}

func (self *TypeA) Operation1() {
    fmt.Println("concrete Operation1")
}

func (self *TypeB) Operation1() {
    fmt.Println("concrete Operation1")
}

func (self *TypeA) Operation2() {
    fmt.Println("concrete Operation2")
}

func (self *TypeB) Operation2() {
    fmt.Println("concrete Operation2")
}

func main() {
    a := NewTypeA()
    a.templateMethod()

    b := NewTypeB()
    b.templateMethod()
}
