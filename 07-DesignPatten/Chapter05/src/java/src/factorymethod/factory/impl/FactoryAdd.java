package factorymethod.factory.impl;

import factorymethod.factory.IFactory;
import factorymethod.product.Operation;
import factorymethod.product.sub.OperationAdd;

public class FactoryAdd implements IFactory {
    public Operation createOperation() {
        return new OperationAdd();
    }
}
