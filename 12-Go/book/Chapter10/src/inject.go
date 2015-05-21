// Package inject provides utilities for mapping and injecting dependencies in various ways.
// inject包提供一个依赖注入器。（其实依赖就是函数的参数，依赖注入就是统一管理函数的参数，并通过反射的方式调用函数）
// 它有以下几个功能：
// 1、统一存储和管理依赖类型和值之间的映射（包括Add(Map, MapTo)，Get）
// 2、将依赖集注入到函数参数中。也即函数调用，从存储中一一找出参数类型对应的值，作为函数的参数来调用它。
// 3、将依赖集注入到struct中
package inject

import (
    "fmt"
    "reflect"
)

// Injector represents an interface for mapping and injecting dependencies into structs
// and function arguments.
// Indector注入器的接口，它的功能就是提供注入器的行为接口。（管理映射，将依赖注入到struct，将依赖注入到函数参数）
type Injector interface {
    Applicator
    Invoker
    TypeMapper
    // SetParent sets the parent of the injector. If the injector cannot find a
    // dependency in its Type map it will check its parent before returning an
    // error.
    // 如果注入器在自己的类型-值映射集中找不到Type对应的Value，在返回错误致歉，继续上溯到父元素继续查找
    SetParent(Injector)
}

// Applicator represents an interface for mapping dependencies to a struct.
// 将依赖注入到struct的接口
type Applicator interface {
    // Maps dependencies in the Type map to each field in the struct
    // that is tagged with 'inject'. Returns an error if the injection
    // fails.
    Apply(interface{}) error
}

// Invoker represents an interface for calling functions via reflection.
// 通过反射调用函数的接口
type Invoker interface {
    // Invoke attempts to call the interface{} provided as a function,
    // providing dependencies for function arguments based on Type. Returns
    // a slice of reflect.Value representing the returned values of the function.
    // Returns an error if the injection fails.
    Invoke(interface{}) ([]reflect.Value, error)
}

// TypeMapper represents an interface for mapping interface{} values based on type.
// 类型-值映射集管理接口
type TypeMapper interface {
    // Maps the interface{} value based on its immediate type from reflect.TypeOf.
    Map(interface{}) TypeMapper
    // Maps the interface{} value based on the pointer of an Interface provided.
    // This is really only useful for mapping a value as an interface, as interfaces
    // cannot at this time be referenced directly without a pointer.
    // 用作将一个值映射为一个接口，因为现在的接口不能被直接引用，即不能MapTo("a", SpecialString)
    // 只能将值映射到接口的引用，MapTo("a", (*SpecialString)(nil))，把nil强制转换为SpecialString
    // 的指针类型
    MapTo(interface{}, interface{}) TypeMapper
    // Returns the Value that is mapped to the current type. Returns a zeroed Value if
    // the Type has not been mapped.
    Get(reflect.Type) reflect.Value
}

type injector struct {
    values map[reflect.Type]reflect.Value
    parent Injector
}

// InterfaceOf dereferences a pointer to an Interface type.
// It panics if value is not an pointer to an interface.
func InterfaceOf(value interface{}) reflect.Type {
    t := reflect.TypeOf(value)

    for t.Kind() == reflect.Ptr {
        t = t.Elem()
    }

    if t.Kind() != reflect.Interface {
        panic("Called inject.InterfaceOf with a value that is not a pointer to an interface. (*MyInterface)(nil)")
    }

    return t
}

// New returns a new Injector.
func New() Injector {
    return &injector{
        values: make(map[reflect.Type]reflect.Value),
    }
}

// Invoke attempts to call the interface{} provided as a function,
// providing dependencies for function arguments based on Type.
// Returns a slice of reflect.Value representing the returned values of the function.
// Returns an error if the injection fails.
// It panics if f is not a function
func (inj *injector) Invoke(f interface{}) ([]reflect.Value, error) {
    t := reflect.TypeOf(f)

    var in = make([]reflect.Value, t.NumIn()) //Panic if t is not kind of Func
    for i := 0; i < t.NumIn(); i++ {
        argType := t.In(i)
        val := inj.Get(argType)
        if !val.IsValid() {
            return nil, fmt.Errorf("Value not found for type %v", argType)
        }

        in[i] = val
    }

    return reflect.ValueOf(f).Call(in), nil
}

// Maps dependencies in the Type map to each field in the struct
// that is tagged with 'inject'.
// Returns an error if the injection fails.
func (inj *injector) Apply(val interface{}) error {
    v := reflect.ValueOf(val)

    for v.Kind() == reflect.Ptr {
        v = v.Elem()
    }

    if v.Kind() != reflect.Struct {
        return nil // Should not panic here ?
    }

    t := v.Type()

    for i := 0; i < v.NumField(); i++ {
        f := v.Field(i)
        structField := t.Field(i)
        if f.CanSet() && structField.Tag == "inject" {
            ft := f.Type()
            v := inj.Get(ft)
            if !v.IsValid() {
                return fmt.Errorf("Value not found for type %v", ft)
            }

            f.Set(v)
        }

    }

    return nil
}

// Maps the concrete value of val to its dynamic type using reflect.TypeOf,
// It returns the TypeMapper registered in.
func (i *injector) Map(val interface{}) TypeMapper {
    i.values[reflect.TypeOf(val)] = reflect.ValueOf(val)
    return i
}

func (i *injector) MapTo(val interface{}, ifacePtr interface{}) TypeMapper {
    i.values[InterfaceOf(ifacePtr)] = reflect.ValueOf(val)
    return i
}

func (i *injector) Get(t reflect.Type) reflect.Value {
    val := i.values[t]
    if !val.IsValid() && i.parent != nil {
        val = i.parent.Get(t)
    }
    return val
}

// func (i *injector) Delete(t reflect.Type) {
//     delete(i.values, t)
// }

func (i *injector) SetParent(parent Injector) {
    i.parent = parent
}
