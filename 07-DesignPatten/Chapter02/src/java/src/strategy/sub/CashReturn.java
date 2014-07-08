package strategy.sub;

import strategy.CashSuper;

//返利收费子类
public class CashReturn implements CashSuper {
    private double mMoneyCondition = 0;
    private double mMoneyReturn = 0;

    public CashReturn(double moneyCondition, double moneyReturn) {
        this.mMoneyCondition = moneyCondition;
        this.mMoneyReturn = moneyReturn;
    }

    public double acceptCash(double money) {
        double result = money;
        if (money >= mMoneyCondition) {
            result = money - money / mMoneyCondition * mMoneyReturn;
        }
        return result;
    }
}
