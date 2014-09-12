package abstractfactory;

import abstractfactory.IDepartment.IDepartment;
import abstractfactory.IUser.IUser;

/**
 * 用反射更灵活
 *
 */
public class DataAccess1 {
    private static final String db = "SqlServer";
    // private static final String db = "Oracle";
    private static String className = null;

    public static IUser createUser() {
        className = db + "User";
        try {
            return (IUser) Class.forName(className).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IDepartment createDepartment() {
        className = db + "Department";
        try {
            return (IDepartment) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
