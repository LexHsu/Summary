package main

import "fmt"
import "reflect"

type TypeBase struct {
    i interface{}
}

func (self *TypeBase) templateMethod() {
    self.operaton1()
    self.operation2()
}

func (self *TypeBase) operation1() {
    if (self.i == nil) {
        return ""
    }
    v := reflect.ValueOf(t.i)
    v.MethodByName("operation1").Call(nil)
}

func (self *TypeBase) operation2() {
    if (self.i == nil) {
        return ""
    }
    v := reflect.ValueOf(t.i)
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
