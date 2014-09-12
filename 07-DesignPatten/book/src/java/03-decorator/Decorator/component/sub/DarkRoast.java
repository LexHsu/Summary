package decorator.component.sub;

import decorator.component.Beverage;

public class DarkRoast extends Beverage {
    public DarkRoast() {
        mDescription = "Dark Roast Coffee";
    }
 
    public double cost() {
        return .99;
    }
}

