package factorymethod.product.sub;

import factorymethod.product.Operation;

public class OperationMul extends Operation {

    @Override
    public double getResult() {
        return mNumberA * mNumberB;
    }

}
