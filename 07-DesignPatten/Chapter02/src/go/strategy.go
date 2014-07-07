package main

import (
    "fmt"
)

type cashSuper interface {
    AcceptCash(float64) float64
}

type cashNormal struct {
}

func (normal *cashNormal) AcceptCash(money float64) float64 {
    return money
}

type cashRebate struct {
    moneyRebate float64
}

func (rebate *cashRebate) AcceptCash(money float64) float64 {
    return money * rebate.moneyRebate
}

type cashReturn struct {
    moneyCondition float64
    moneyReturn    float64
}

func (returned *cashReturn) AcceptCash(money float64) float64 {
    if money >= returned.moneyCondition {
        return money - float64(int(money/returned.moneyCondition))*returned.moneyReturn
    } else {
        return money
    }
}

type CashContext struct {
    cashSuper
}

func NewCashContext(str string) *CashContext {
    cash := new(CashContext)
    switch str {
    case "正常收费":
        cash.cashSuper = &cashNormal{}
    case "满300返100":
        cash.cashSuper = &cashReturn{300, 100}
    case "打8折":
        cash.cashSuper = &cashRebate{0.8}
    }
    return cash
}

func main() {
    var total float64
    context := NewCashContext("满300返100")
    total += context.AcceptCash(1 * 10000)
    context = NewCashContext("正常收费")
    total += context.AcceptCash(1 * 10000)
    context = NewCashContext("打8折")
    total += context.AcceptCash(1 * 10000)
    fmt.Println(total)
}
