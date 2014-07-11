package factorymethod.factory.impl;

import factorymethod.factory.IFactory;
import factorymethod.product.Operation;
import factorymethod.product.sub.OperationDiv;

public class FactoryDiv implements IFactory {
    public Operation createOperation() {
        return new OperationDiv();
    }
}
