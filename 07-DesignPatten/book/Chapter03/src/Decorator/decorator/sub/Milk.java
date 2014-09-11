package decorator.decorator.sub;

import decorator.component.Beverage;
import decorator.decorator.CondimentDecorator;

public class Milk extends CondimentDecorator {
    Beverage mBeverage;

    public Milk(Beverage beverage) {
        this.mBeverage = beverage;
    }

    public String getDescription() {
        return mBeverage.getDescription() + ", Milk";
    }

    public double cost() {
        return .10 + mBeverage.cost();
    }
}
