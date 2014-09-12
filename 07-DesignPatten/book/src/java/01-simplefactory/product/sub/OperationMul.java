package simplefactory.product.sub;

import simplefactory.product.Operation;

public class OperationMul extends Operation {

    @Override
    public double getResult() {
        return mNumberA * mNumberB;
    }

}
