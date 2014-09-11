package main

import (
    "fmt"
    //"reflect"
)

type User struct {
    id   int
    name string
}

func (this *User) getId() int {
    return this.id
}

func (this *User) SetId(id int) {
    this.id = id
}

func (this *User) getName() string {
    return this.name
}

func (this *User) setName(name string) {
    this.name = name
}

type IUser interface {
    insert(*User)
    getUser(int) *User
}

type SqlServerUser struct {
}

func (this *SqlServerUser) insert(user *User) {
    fmt.Println("在SQL Server中给User表增加一条记录")
}

func (this *SqlServerUser) getUser(id int) *User {
    fmt.Println("在SQL Server中根据ID得到User表一条记录")
    return nil
}

type AccessUser struct {
}

func (this *AccessUser) insert(user *User) {
    fmt.Println("在SQL Server中给User表增加一条记录")
}

func (this *AccessUser) getUser(id int) *User {
    fmt.Println("在SQL Server中根据ID得到User表一条记录")
    return nil
}

type IFactory interface {
    createUser() IUser
    createDepartment() IDepartment
}

type SqlServerFactory struct {
}

func (this *SqlServerFactory) createUser() IUser {
    return new(SqlServerUser)
}

func (this *SqlServerFactory) createDepartment() IDepartment {
    return new(SqlServerDepartment)
}

type AccessFactory struct {
}

func (this *AccessFactory) createUser() IUser {
    return new(AccessUser)
}

func (this *AccessFactory) createDepartment() IDepartment {
    return new(AccessDepartment)
}

type Department struct {
    id   int
    name string
}

func (this *Department) getId() int {
    return this.id
}

func (this *Department) SetId(id int) {
    this.id = id
}

func (this *Department) getName() string {
    return this.name
}

func (this *Department) setName(name string) {
    this.name = name
}

type IDepartment interface {
    insert(department *Department)
    getDepartment(id int) *Department
}

type SqlServerDepartment struct {
}

func (this *SqlServerDepartment) insert(department *Department) {
    fmt.Println("在SQL Server中给Deaprtment表增加一条记录")
}

func (this *SqlServerDepartment) getDepartment(id int) *Department {
    fmt.Println("在SQL Server中根据ID得到Deaprtment表一条记录")
    return nil
}

type AccessDepartment struct {
}

func (this *AccessDepartment) insert(department *Department) {
    fmt.Println("在SQL Server中给Deaprtment表增加一条记录")
}

func (this *AccessDepartment) getDepartment(id int) *Department {
    fmt.Println("在SQL Server中根据ID得到Deaprtment表一条记录")
    return nil
}

type DataAccess struct {
    db string
}

func (this *DataAccess) createUser() (iuser IUser) {
    if this.db == "Sqlserver" {
        iuser = new(SqlServerUser)
    } else if this.db == "Access" {
        iuser = new(AccessUser)
    }
    return
}

func (this *DataAccess) createDepartment() (id IDepartment) {
    if this.db == "Sqlserver" {
        id = new(SqlServerDepartment)
    } else if this.db == "Access" {
        id = new(AccessDepartment)
    }
    return
}

func main() {
    database := &DataAccess{"Sqlserver"}
    user := new(User)
    department := new(Department)
    iu := database.createUser()
    iu.insert(user)
    iu.getUser(1)

    id := database.createDepartment()
    id.insert(department)
    id.getDepartment(1)
}
