package factorymethod;

import factorymethod.factory.IFactory;
import factorymethod.factory.impl.FactoryAdd;
import factorymethod.product.Operation;

public class Client {
    public static void main(String[] args) {
        IFactory operFactory = new FactoryAdd();
        Operation oper = operFactory.createOperation();

        oper.setNumberA(1);
        oper.setNumberB(2);

        try {
            double result = oper.getResult();
            System.out.println("Result is: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
