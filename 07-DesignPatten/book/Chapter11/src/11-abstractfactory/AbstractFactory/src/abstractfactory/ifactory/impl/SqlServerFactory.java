package abstractfactory.ifactory.impl;

import abstractfactory.IDepartment.IDepartment;
import abstractfactory.IDepartment.impl.SqlServerDepartment;
import abstractfactory.IUser.IUser;
import abstractfactory.IUser.impl.SqlServerUser;
import abstractfactory.ifactory.IFactory;

/**
 * 实现 IFactory 接口，实例化 SqlServerUser 和 SqlServerDepartment
 *
 */
public class SqlServerFactory implements IFactory {
    public IUser createUser() {
        return new SqlServerUser();
    }

    public IDepartment createDepartment() {
        return new SqlServerDepartment();
    }
}
