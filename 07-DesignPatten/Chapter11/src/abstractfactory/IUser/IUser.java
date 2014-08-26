package abstractfactory.IUser;

import abstractfactory.User;

/**
 * 用于客户端访问，解除与具体数据库访问的耦合
 *
 */
public interface IUser {
    void insert(User user);

    User getUser(int id);
}
