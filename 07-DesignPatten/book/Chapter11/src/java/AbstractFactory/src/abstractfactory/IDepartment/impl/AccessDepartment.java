package abstractfactory.IDepartment.impl;

import abstractfactory.Department;
import abstractfactory.IDepartment.IDepartment;

/**
 * 用于访问 Access 的 Department
 */
public class AccessDepartment implements IDepartment {
    public void insert(Department department) {
        System.out.println("在Access中给Deaprtment表增加一条记录");
    }

    public Department getDepartment(int id) {
        System.out.println("在Access中根据ID得到Deaprtment表一条记录");
        return null;
    }
}