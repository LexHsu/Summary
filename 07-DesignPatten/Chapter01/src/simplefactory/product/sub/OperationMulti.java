package simplefactory.product.sub;

import simplefactory.product.Operation;

public class OperationMulti extends Operation {

    @Override
    public double getResult() {
        return mNumberA * mNumberB;
    }

}
