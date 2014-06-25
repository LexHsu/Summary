package decorator.decorator.sub;

import decorator.component.Beverage;
import decorator.decorator.CondimentDecorator;
 
public class Whip extends CondimentDecorator {
    Beverage mBeverage;
 
    public Whip(Beverage beverage) {
        this.mBeverage = beverage;
    }
 
    public String getDescription() {
        return mBeverage.getDescription() + ", Whip";
    }
 
    public double cost() {
        return .10 + mBeverage.cost();
    }
}
