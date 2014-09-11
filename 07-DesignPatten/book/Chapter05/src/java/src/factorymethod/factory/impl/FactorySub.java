package factorymethod.factory.impl;

import factorymethod.factory.IFactory;
import factorymethod.product.Operation;
import factorymethod.product.sub.OperationSub;

public class FactorySub implements IFactory {
    public Operation createOperation() {
        return new OperationSub();
    }
}
