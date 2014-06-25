package decorator.component.sub;

import decorator.component.Beverage;

public class Decaf extends Beverage {
    public Decaf() {
        mDescription = "Decaf Coffee";
    }
 
    public double cost() {
        return 1.05;
    }
}

