package decorator.decorator.sub;

import decorator.component.Beverage;
import decorator.decorator.CondimentDecorator;

public class Soy extends CondimentDecorator {
    Beverage mBeverage;

    public Soy(Beverage beverage) {
        this.mBeverage = beverage;
    }

    public String getDescription() {
        return mBeverage.getDescription() + ", Soy";
    }

    public double cost() {
        return .15 + mBeverage.cost();
    }
}
