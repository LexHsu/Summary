package abstractfactory;

import abstractfactory.IDepartment.IDepartment;
import abstractfactory.IUser.IUser;

public class Client {
    public static void main(String[] args) {
        User user = new User();
        Department department = new Department();

        IUser iu = DataAccess.createUser();
        // IUser iu = DataAccess1.createUser();

        iu.insert(user);
        iu.getUser(1);

        IDepartment id = DataAccess.createDepartment();
        // IDepartment id = DataAccess1.createDepartment();

        id.insert(department);
        id.getDepartment(1);
    }
}
