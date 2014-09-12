package simplefactory;

import java.util.Scanner;

import simplefactory.factory.OperationFactory;
import simplefactory.product.Operation;

public class Test {
 
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Please input number A:");
        String strNumberA = scanner.next();
        
        System.out.println("Please input number B:");
        String strNumberB = scanner.next();
        
        System.out.println("Please input operate character:");
        String operate = scanner.next();
        
        try {
            Double numberA = Double.parseDouble(strNumberA);
            Double numberB = Double.parseDouble(strNumberB);
            Operation operation = OperationFactory.createOperation(operate);
            if (operation == null) {
                System.out.println("operation is null.");
                return;
            }
            operation.setNumberA(numberA);
            operation.setNumberB(numberB);
            double result = operation.getResult();
            System.out.println("Result is:" + result);
        } catch(NumberFormatException e) {
            System.out.println("Format exception: " + e.toString());
        } catch (Exception e) {
            System.out.println("Divisor exception: " + e.toString());
        } finally {
            scanner.close();
        }
    }
}
