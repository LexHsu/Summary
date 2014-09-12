package abstractfactory.ifactory.impl;

import abstractfactory.IDepartment.IDepartment;
import abstractfactory.IDepartment.impl.AccessDepartment;
import abstractfactory.IUser.IUser;
import abstractfactory.IUser.impl.AccessUser;
import abstractfactory.ifactory.IFactory;

/**
 * 实现 IFactory 接口，实例化 AccessUser 和 AccessDepartment
 *
 */
public class AccessFactory implements IFactory {
    public IUser createUser() {
        return new AccessUser();
    }

    public IDepartment createDepartment() {
        return new AccessDepartment();
    }
}
