package main

import "fmt"

type Rentable interface {
    rent()
}

type Landlord struct {
    name string
}

func (self *Landlord) rent() {
    fmt.Println("i have a room for rent.", self.name)
}

type Agent struct {
    landlord Landlord
}

func (self *Agent) rent() {
    self.landlord.rent()
}

func CreateAgent(landlord Landlord) *Agent {
    return &Agent{landlord}
}

func main() {
    landlord := Landlord{"Alice"}
    agent := CreateAgent(landlord)
    agent.rent()
}
