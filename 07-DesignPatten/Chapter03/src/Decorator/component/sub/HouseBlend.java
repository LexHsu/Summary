package decorator.component.sub;

import decorator.component.Beverage;

public class HouseBlend extends Beverage {
    public HouseBlend() {
        mDescription = "House Blend Coffee";
    }
 
    public double cost() {
        return .89;
    }
}

