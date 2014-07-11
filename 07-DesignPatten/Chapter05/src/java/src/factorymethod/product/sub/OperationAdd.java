package factorymethod.product.sub;

import factorymethod.product.Operation;

public class OperationAdd extends Operation {

    @Override
    public double getResult() {
        return mNumberA + mNumberB;
    }

}
