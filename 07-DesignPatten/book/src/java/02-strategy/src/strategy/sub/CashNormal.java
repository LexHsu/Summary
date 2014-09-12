package strategy.sub;

import strategy.CashSuper;

public class CashNormal implements CashSuper {

    public double acceptCash(double money) {
        return money;
    }
}
