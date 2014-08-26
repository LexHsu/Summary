package abstractfactory.ifactory;

import abstractfactory.IDepartment.IDepartment;
import abstractfactory.IUser.IUser;

/**
 * 定义一个创建访问 User 表对象的抽象工厂接口
 *
 */
public interface IFactory {
    IUser createUser();

    IDepartment createDepartment();
}