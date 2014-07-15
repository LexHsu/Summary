package main

import "fmt"
import "reflect"

type TypeBase struct {
    i interface{}
}

func (self *TypeBase) templateMethod() {
    self.operation1()
    self.operation2()
}

func (self *TypeBase) Operation1() {
    if (self.i == nil) {
        return
    }
    v := reflect.ValueOf(self.i)
    v.MethodByName("operation1").Call(nil)
}

func (self *TypeBase) Operation2() {
    if (self.i == nil) {
        return
    }
    v := reflect.ValueOf(self.i)
    v.MethodByName("operation2").Call(nil)
}

type TypeA struct {
    TypeBase
}

type TypeB struct {
    TypeBase
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
