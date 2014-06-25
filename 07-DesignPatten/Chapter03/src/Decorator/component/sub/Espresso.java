package decorator.component.sub;

import decorator.component.Beverage;

public class Espresso extends Beverage {
  
    public Espresso() {
        mDescription = "Espresso";
    }
  
    public double cost() {
        return 1.99;
    }
}

