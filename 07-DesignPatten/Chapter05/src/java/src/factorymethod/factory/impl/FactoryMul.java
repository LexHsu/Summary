package factorymethod.factory.impl;

import factorymethod.factory.IFactory;
import factorymethod.product.Operation;
import factorymethod.product.sub.OperationMul;

public class FactoryMul implements IFactory {
    public Operation createOperation() {
        return new OperationMul();
    }
}
