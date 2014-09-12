package main

import (
    "./10-tbs"
    "fmt"
)

const EVEN_TYPE = "state"

type Subject interface {
    Notify()
    changeState(string)
    getState() string
}

type ConcreteObserver struct {
    name string
    Subject
}

type ConcreteSubject struct {
    dispatcher *tbs.Dispatcher
    action string
}

func (this *ConcreteObserver) Update(event *tbs.Event) {
    fmt.Println(this.getState(), this.name)
}

func CreateSubject() *ConcreteSubject {
    ConcreteSubject := new(ConcreteSubject)
    ConcreteSubject.dispatcher = tbs.SharedDispatcher()
    return ConcreteSubject
}

func (this *ConcreteSubject) Notify() {
    // 事件携带的参数
    params := make(map[string]interface{})
    params["id"] = 1000
    event := tbs.CreateEvent(EVEN_TYPE, params)
    this.dispatcher.DispatchEvent(event)
}

func (this *ConcreteSubject) changeState(value string) {
    this.action = value
}

func (this *ConcreteSubject) getState() string {
    return this.action
}

func main() {
    done := make(chan bool, 1)
    subject := CreateSubject()
    observer1 := &ConcreteObserver{"observer1", subject}
    observer2 := &ConcreteObserver{"observer2", subject}

    var callback1 tbs.EventCallback = observer1.Update
    subject.dispatcher.AddEventListener(EVEN_TYPE, &callback1)

    var callback2 tbs.EventCallback = observer2.CloseNBADirectSeeding
    subject.dispatcher.AddEventListener(EVEN_TYPE, &callback2)
    go func() {
        fmt.Println("--- change state ---")
        subject.changeState("new state")
        subject.Notify()

        done <- true
    }()
    <-done

    fmt.Println("--- remove observer1 ---")
    subject.dispatcher.RemoveEventListener(EVEN_TYPE, &callback1)
    go func() {
        subject.changeState("new state2")
        subject.Notify()
        done <- true
    }
    <-done
}
