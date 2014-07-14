package main

import "fmt"
import "reflect"

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
    self.i.(Oper).operation1()
    self.i.(Oper).operation2()
}

func (self *TypeA) operation1() {
    fmt.Println("concrete operation1")
}

func (self *TypeB) operation1() {
    fmt.Println("concrete operation1")
}

func (self *TypeA) operation2() {
    fmt.Println("concrete operation2")
}

func (self *TypeB) operation2() {
    fmt.Println("concrete operation2")
}

func main() {
    a := NewTypeA()
    a.templateMethod()

    b := NewTypeB()
    b.templateMethod()
}
