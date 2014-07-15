package main

import "fmt"

type System1 struct {
}

type System2 struct {
}

type System3 struct {
}

type Facade struct {
    s1 System1
    s2 System2
    s3 System3
}

func (self *System1) method1() {
    fmt.Println("method1")
}

func (self *System2) method2() {
    fmt.Println("method1")
}

func (self *System3) method3() {
    fmt.Println("method1")
}

func (self *Facade) methodGroup1() {
    self.s1.method1()
    self.s2.method2()
}

func (self *Facade) methodGroup2() {
    self.s2.method1()
    self.s3.method2()
}

func NewFacade() {
    return &Facade{System1{}, System2{}, System3{}}
}

func main() {
    facade := NewFacade()
    facade.methodGroup1()
    facade.methodGroup2()
}
