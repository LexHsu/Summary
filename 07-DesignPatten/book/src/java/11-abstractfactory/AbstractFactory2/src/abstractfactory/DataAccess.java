package abstractfactory;

import abstractfactory.IDepartment.IDepartment;
import abstractfactory.IDepartment.impl.AccessDepartment;
import abstractfactory.IDepartment.impl.SqlServerDepartment;
import abstractfactory.IUser.IUser;
import abstractfactory.IUser.impl.AccessUser;
import abstractfactory.IUser.impl.SqlServerUser;

public class DataAccess {
    private static final String db = "Sqlserver";

    public static IUser createUser() {
        IUser result = null;
        if ("Sqlserver".equals(db)) {
            result = new SqlServerUser();
        } else if ("Access".equals(db)) {
            result = new AccessUser();
        }

        return result;
    }

    public static IDepartment createDepartment() {
        IDepartment result = null;
        if ("Sqlserver".equals(db)) {
            result = new SqlServerDepartment();
        } else if ("Access".equals(db)) {
            result = new AccessDepartment();
        }

        return result;
    }
}
