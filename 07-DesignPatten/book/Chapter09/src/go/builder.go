package main

import (
    "fmt"
    "reflect"
    // "unsafe"
)

type Director struct {
    Builder
}

type Builder interface {
    buildPart1()
    buildPart2()
    getResult() *Product
}

type ConcreteBuilder struct {
    product *Product
}

type Product struct {
    part1, part2 string
}

func (self *ConcreteBuilder) buildPart1() {
    self.product.part1 = "build part1..."
}

func (self *ConcreteBuilder) buildPart2() {
    self.product.part2 = "build part2..."
}

func (p *ConcreteBuilder) getResult() *Product {
    return p.product
}

func NewConcreteBuilder() *ConcreteBuilder {
    return &ConcreteBuilder{new(Product)}
}

func (p *Director) construct() {
    p.buildPart1()
    p.buildPart2()
}

func (p *Director) getResult() *Product {
    return p.Builder.getResult()
}

func main() {
    builder := NewConcreteBuilder()
    director := &Director{builder}
    director.construct()
    product := director.getResult()
    value := reflect.ValueOf(*product)
    for i := 0; i < value.NumField(); i++ {
        fmt.Println(value.Field(i))
    }

    // x := unsafe.Pointer(product)
    // fmt.Println(*(*string)(unsafe.Pointer(uintptr(x))))
    // fmt.Println(*(*string)(unsafe.Pointer(uintptr(x) + unsafe.Offsetof(product.part1))))
    // fmt.Println(*(*string)(unsafe.Pointer(uintptr(x) + unsafe.Offsetof(product.part2))))
    // fmt.Println(product)
}
