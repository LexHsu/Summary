package simplefactory.product.sub;

import simplefactory.product.Operation;

public class OperationDiv extends Operation {

    private static final double NUMBER_ZERO = 0d;
    @Override
    public double getResult() throws Exception{
        if (mNumberB == NUMBER_ZERO) {
            throw new Exception("divisor can not be zero.");
        }
        return mNumberA / mNumberB;
    }

}
