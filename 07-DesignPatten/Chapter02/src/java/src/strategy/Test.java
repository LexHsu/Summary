package strategy;

import context.CashContext;
import strategy.sub.CashNormal;
import strategy.sub.CashRebate;
import strategy.sub.CashReturn;

public class Test {
    private static final String CASH_NORMAL = "正常收费";
    private static final String CASH_DEBATE = "满300返100";
    private static final String CASH_RETURN = "打8折";

    public static void main(String[] args) {

        double total = 0d;
        total = consume(CASH_NORMAL, 1, 1000);
        total += consume(CASH_DEBATE, 1, 1000);
        total += consume(CASH_RETURN, 1, 1000);

        System.out.println("total:" + (int) total);
    }

    public static double consume(String type, int num, double price) {
        CashContext cashContext = null;

        do {
            if (CASH_NORMAL.equals(type)) {
                cashContext = new CashContext(new CashNormal());
                break;
            }
            if (CASH_DEBATE.equals(type)) {
                cashContext = new CashContext(new CashReturn(300, 100));
                break;
            }
            if (CASH_RETURN.equals(type)) {
                cashContext = new CashContext(new CashRebate(0.8));
                break;
            }
        } while (false);

        double total = cashContext.acceptCash(num * price);

        System.out.println("price:" + price + " number:" + num + "sum:" + total);
        return total;
    }
}