package decorator;

import decorator.component.Beverage;
import decorator.component.sub.DarkRoast;
import decorator.component.sub.Espresso;
import decorator.component.sub.HouseBlend;
import decorator.decorator.sub.Mocha;
import decorator.decorator.sub.Soy;
import decorator.decorator.sub.Whip;

public class StarbuzzCoffee {
 
    public static void main(String args[]) {
        Beverage beverage = new Espresso();
        System.out.println(beverage.getDescription() 
                + " $" + beverage.cost());
 
        Beverage beverage2 = new DarkRoast();
        beverage2 = new Mocha(beverage2);
        beverage2 = new Mocha(beverage2);
        beverage2 = new Whip(beverage2);
        System.out.println(beverage2.getDescription() 
                + " $" + beverage2.cost());
 
        Beverage beverage3 = new HouseBlend();
        beverage3 = new Soy(beverage3);
        beverage3 = new Mocha(beverage3);
        beverage3 = new Whip(beverage3);
        System.out.println(beverage3.getDescription() 
                + " $" + beverage3.cost());
    }
}
