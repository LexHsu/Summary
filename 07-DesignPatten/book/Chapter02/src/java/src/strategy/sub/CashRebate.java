package strategy.sub;

import strategy.CashSuper;

public class CashRebate implements CashSuper {
    private double mMoneyRebate = 1;

    public CashRebate(double moneyRebate) {
        this.mMoneyRebate = moneyRebate;
    }

    public double acceptCash(double money) {
        return money * mMoneyRebate;
    }
}
