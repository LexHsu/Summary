package simplefactory.factory;

import simplefactory.product.Operation;
import simplefactory.product.sub.OperationAdd;
import simplefactory.product.sub.OperationDiv;
import simplefactory.product.sub.OperationSub;
import simplefactory.product.sub.OperationMul;

public class OperationFactory {

    private static final String OPERATE_ADD = "+";
    private static final String OPERATE_SUB = "-";
    private static final String OPERATE_MUL = "*";
    private static final String OPERATE_DIV = "/";
    
    public static Operation createOperation(String operate) {
        Operation operation = null;
        do {
            if (OPERATE_ADD.equals(operate)) {
                operation = new OperationAdd();
                break;
            }
            
            if (OPERATE_SUB.equals(operate)) {
                operation = new OperationSub();
                break;
            }
            
            if (OPERATE_MUL.equals(operate)) {
                operation = new OperationMul();
                break;
            }
            
            if (OPERATE_DIV.equals(operate)) {
                operation = new OperationDiv();
                break;
            }
            System.out.println("no matched operation");
        } while (false);

        return operation;
    }
}
