package abstractfactory.IUser.impl;

import abstractfactory.User;
import abstractfactory.IUser.IUser;

/**
 * 用于访问 SQL Server 的 User
 *
 */
public class SqlServerUser implements IUser {
    public void insert(User user) {
        System.out.println("在SQL Server中给User表增加一条记录");
    }

    public User getUser(int id) {
        System.out.println("在SQL Server中根据ID得到User表一条记录");
        return null;
    }
}
