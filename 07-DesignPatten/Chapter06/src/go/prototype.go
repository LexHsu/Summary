package main

import "fmt"

type Experience struct {
    timeArea string
    company  string
}

func (self *Experience) getWorkDate() string {
    return self.timeArea
}

func (self *Experience) setWorkDate(timeArea string) {
    self.timeArea = timeArea
}

func (self *Experience) getCompany() string {
    return self.company
}

func (self *Experience) setCompany(company string) {
    self.company = company
}

type Resume struct {
    name string
    sex  string
    age  string
    Experience
}

func (self *Resume) setInfo(name, sex, age string) {
    self.name = name
    self.age = age
    self.sex = sex
}

func (self *Resume) setExperience(timeArea, company string) {
    self.company = company
    self.timeArea = timeArea
}

func (self *Resume) display() {
    fmt.Println(self.name, self.sex, self.age)
    fmt.Println("Experience：", self.timeArea, self.company)
}

func (self *Resume) clone() *Resume {
    obj := (*self)
    return &obj
}

func main() {
    a := new(Resume)
    a.setInfo("Jack", "Man", "29")
    a.setExperience("1998-2000", "Google")

    b := a.clone()
    b.setInfo("Lion", "Man", "49")
    b.setExperience("1988-2010", "Twitter")

    c := a.clone()
    c.setInfo("Len", "女", "19")
    c.setExperience("2009-2010", "Amazon")

    a.display()
    b.display()
    c.display()
}
