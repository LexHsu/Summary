package context;

import strategy.CashSuper;

public class CashContext {
    CashSuper mCashSuper;

    public CashContext(CashSuper cashSuper) {
        this.mCashSuper = cashSuper;
    }

    public double acceptCash(double money) {
        return mCashSuper.acceptCash(money);
    }
}
