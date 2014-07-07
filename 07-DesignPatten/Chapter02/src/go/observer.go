package main

import (
    "./tbs"
    "fmt"
)

type StockObserver struct {
    name string
    Subject
}

func (this *StockObserver) CloseStockMarket(event *tbs.Event) {
    fmt.Println(this.getState(), this.name, "关闭股票行情，继续工作！")
}

type NBAObserver struct {
    name string
    Subject
}

func (this *NBAObserver) CloseNBADirectSeeding(event *tbs.Event) {
    fmt.Println(this.getState(), this.name, "关闭NBA直播，继续工作！")
}

type Subject interface {
    Notify()
    setState(string)
    getState() string
}

type Secretary struct {
    dispatcher *tbs.Dispatcher
    action     string
}

func (this *Secretary) Notify() {
    //随便弄个事件携带的参数，我把参数定义为一个map
    params := make(map[string]interface{})
    params["id"] = 1001
    //创建一个事件对象
    event := tbs.CreateEvent("临时抽查", params)
    this.dispatcher.DispatchEvent(event)
}

func (this *Secretary) setState(value string) {
    this.action = value
}

func (this *Secretary) getState() string {
    return this.action
}

func NewSecretary() *Secretary {
    secretary := new(Secretary)
    secretary.dispatcher = tbs.SharedDispatcher()
    return secretary
}

type Boss struct {
    dispatcher *tbs.Dispatcher
    action     string
}

func NewBoss() *Boss {
    boss := new(Boss)
    boss.dispatcher = tbs.SharedDispatcher()
    return boss
}

func (this *Boss) Notify() {
    //随便弄个事件携带的参数，我把参数定义为一个map
    params := make(map[string]interface{})
    params["id"] = 1000
    //创建一个事件对象
    event := tbs.CreateEvent("临时抽查", params)
    this.dispatcher.DispatchEvent(event)
}

func (this *Boss) setState(value string) {
    this.action = value
}

func (this *Boss) getState() string {
    return this.action
}

func main() {
    done := make(chan bool, 1)
    go func() {
        huhansan := NewBoss()
        sec := NewSecretary()
        tongshi3 := &NBAObserver{"李劲松", sec}
        tongshi1 := &StockObserver{"魏关姹", huhansan}
        tongshi2 := &NBAObserver{"易管查", huhansan}

        var cb tbs.EventCallback = tongshi1.CloseStockMarket
        huhansan.dispatcher.AddEventListener("临时抽查", &cb)

        var cb2 tbs.EventCallback = tongshi2.CloseNBADirectSeeding
        huhansan.dispatcher.AddEventListener("临时抽查", &cb2)

        var cb3 tbs.EventCallback = tongshi3.CloseNBADirectSeeding
        sec.dispatcher.AddEventListener("临时抽查", &cb3)

        huhansan.setState("我胡汉三回来了！")
        sec.setState("我陈美嘉回来了！")
        sec.Notify()
        huhansan.Notify()
        huhansan.dispatcher.RemoveEventListener("临时抽查", &cb)
        huhansan.Notify()
        done <- true
    }()
    <-done
}
