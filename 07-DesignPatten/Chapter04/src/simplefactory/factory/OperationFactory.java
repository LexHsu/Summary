package simplefactory.factory;

import simplefactory.product.Operation;
import simplefactory.product.sub.OperationAdd;
import simplefactory.product.sub.OperationDivi;
import simplefactory.product.sub.OperationMinus;
import simplefactory.product.sub.OperationMulti;

public class OperationFactory {

    private static final String OPERATE_PLUS = "+";
    private static final String OPERATE_MINUS = "-";
    private static final String OPERATE_MULTI = "*";
    private static final String OPERATE_DIVISION = "/";
    
    public static Operation createOperation(String operate) {
        Operation operation = null;
        do {
            if (OPERATE_PLUS.equals(operate)) {
                operation = new OperationAdd();
                break;
            }
            
            if (OPERATE_MINUS.equals(operate)) {
                operation = new OperationMinus();
                break;
            }
            
            if (OPERATE_MULTI.equals(operate)) {
                operation = new OperationMulti();
                break;
            }
            
            if (OPERATE_DIVISION.equals(operate)) {
                operation = new OperationDivi();
                break;
            }
            System.out.println("no matched operation");
        } while (false);

        return operation;
    }
}
