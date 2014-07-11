package factorymethod.factory;

import factorymethod.product.Operation;

public interface IFactory {
    Operation createOperation();
}