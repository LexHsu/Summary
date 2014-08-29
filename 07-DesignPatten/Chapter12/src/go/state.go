package main

import (
    "fmt"
)

type State interface {
    writeProgram(Work)
}

// 上午工作状态类
type ForenoonState struct {
}

func (this *ForenoonState) writeProgram(work Work) {
    if work.getHour() < 12 {
        fmt.Println("Time：", work.getHour(), ", Forenoon State.")
    } else {
        work.setState(new(NoonState))
        work.writeProgram()
    }
}

// 下午工作状态
type AfternoonState struct {
}

func (this *AfternoonState) writeProgram(work Work) {
    if work.getHour() < 17 {
        fmt.Println("Time：", work.getHour(), ", Afternoon State.")
    } else {
        work.setState(new(EveningState))
        work.writeProgram()
    }
}

// 下班休息状态
type RestState struct {
}

func (this *RestState) writeProgram(work Work) {
    fmt.Println("Time：", work.getHour(), ", Rest State.")
}

// 工作类，此时没有了过长的分支判断语句
type Work struct {
    hour   int
    finish bool
    state  State
}

func NewWork() *Work {
    state := new(ForenoonState)
    return &Work{state: state}
}

func (w *Work) writeProgram() {
    w.state.writeProgram(*w)
}

func (w *Work) getHour() int {
    return w.hour
}

func (w *Work) setHour(hour int) {
    w.hour = hour
}

func (w *Work) isFinish() bool {
    return w.finish
}

func (w *Work) setFinish(finish bool) {
    w.finish = finish
}

func (w *Work) getState() State {
    return w.state
}

func (w *Work) setState(state State) {
    w.state = state
}

func main() {
    work := NewWork()
    work.setHour(9)
    work.writeProgram()

    work.setHour(14)
    work.writeProgram()

    work.setHour(17)
    work.setFinish(true)
    work.writeProgram()

    work.setHour(19)
    work.writeProgram()
}
