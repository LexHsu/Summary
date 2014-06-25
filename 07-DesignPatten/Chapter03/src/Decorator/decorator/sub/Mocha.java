package decorator.decorator.sub;

import decorator.component.Beverage;
import decorator.decorator.CondimentDecorator;

public class Mocha extends CondimentDecorator {
    Beverage mBeverage;
 
    public Mocha(Beverage beverage) {
        this.mBeverage = beverage;
    }
 
    public String getDescription() {
        return mBeverage.getDescription() + ", Mocha";
    }
 
    public double cost() {
        return .20 + mBeverage.cost();
    }
}
