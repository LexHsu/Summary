抽象工厂模式
===

### 模式定义

抽象工厂模式（Abstract Factory），提供一个创建一系列相关或相互依赖对象的接口，而无需指定它们具体的类。

### UML 类图

![Alt text](img/abstractfactory.png)

### 代码示例

```java
public interface IFactory {
    IUser createUser();

    IDepartment createDepartment();
}

public class SqlServerFactory implements IFactory {
    public IUser createUser() {
        return new SqlServerUser();
    }

    public IDepartment createDepartment() {
        return new SqlServerDepartment();
    }
}

public interface IDepartment {
    void insert(Department department);

    Department getDepartment(int id);
}

public class SqlServerDepartment implements IDepartment {
    public void insert(Department department) {
        System.out.println("在SQL Server中给Deaprtment表增加一条记录");
    }

    public Department getDepartment(int id) {
        System.out.println("在SQL Server中根据ID得到Deaprtment表一条记录");
        return null;
    }
}

public interface IUser {
    void insert(User user);

    User getUser(int id);
}

public class SqlServerUser implements IUser {
    public void insert(User user) {
        System.out.println("在SQL Server中给User表增加一条记录");
    }

    public User getUser(int id) {
        System.out.println("在SQL Server中根据ID得到User表一条记录");
        return null;
    }
}

public class Client {
    public static void main(String[] args) {
        User user = new User();
        Department department = new Department();

        IFactory factory = new SqlServerFactory();
        // IFactory factory = new AccessFactory();

        IUser iu = factory.createUser();

        iu.insert(user);
        iu.getUser(1);

        IDepartment id = factory.createDepartment();

        id.insert(department);
        id.getDepartment(1);
    }
}
```

ProductA1 为 SqlServerUser，而 ProductB1 为 AccessUser。
IFactory 是一个抽象工厂接口，其包含所有的产品创建的抽象方法。
而具体工厂即 SqlServerFactory 和 AccessFactory。
通常在运行时创建一个 ConcreteFactory 类的实例，该具体工厂再创建具有特定实现的产品对象，即创建不同的产品对象，应使用不同的具体工厂。

### 抽象工厂模式的优缺点

该模式最大优点是易于交换产品系列，由于具体工厂类如 IFactory factory = new AccessFactory()，
在应用中只需初始化一次，使得改变一个应用的具体工厂变得非常容易。

还有一个优点是具体的创建实例过程与客户端分离，客户端是通过它们的抽象接口操纵实例，产品的具体类名也被具体工厂的实现分离，不会出现在客户端代码中。
如上例，Client 类仅需关注 IUser 和 IDepartment，不关心它是用 SQL Server 实现还是用 Access 来实现。

该模式缺点也明显，如抽象工厂模式可以很方便地切换两个数据库访问的代码，但是如果要增加功能，如项目表 Project，需要至少增加三个类，Iproject、SqlServerProject、AccessProject，
还需要更改 IFactory、SqlServerFactory 和 AccessFactory 才可以完全实现。

其次，真实项目中 Client 类不会只有一个，有很多地方都在使用 IUser 或 IDepartment，在每一个类的开始都需要修改 IFactory factory = new SqlServerFactory()。

### 用简单工厂改进抽象工厂

去掉 IFactory、SqlServerFactory 和 AccessFactory 类，新增简单工厂 DataAccess 类代替原来的 工厂类。

```java
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
```

这样 Client 只需DataAccess.createUser() 与 DataAccess.createDepartment()即可生成具体的数据库访问类实例，
没有出现任何一个SQL Server或Access的字样，耦合度更低。

### 用反射及抽象工厂

上述优化方案还是没有解决新增产品类不方便的问题。用反射可解决该问题。

```java
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
```

通过反射，程序由编译时变为运行时。而反射中的字符串是可写成变量，变量值到是 SQLServer，还是 Access 完全可以由事件的那个db变量来决定。
解决了 if 语句硬编码扩展性差的问题。若要新增 Oracle，仅需要 SqlServer 改为 Oracle 即可。或存入配置文件，扩展性更好。
