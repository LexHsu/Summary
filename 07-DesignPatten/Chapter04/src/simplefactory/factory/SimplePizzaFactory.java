package simplefactory.factory;

import simplefactory.product.Pizza;
import simplefactory.product.sub.CheesePizza;
import simplefactory.product.sub.ClamPizza;
import simplefactory.product.sub.PepperoniPizza;
import simplefactory.product.sub.VeggiePizza;

public class SimplePizzaFactory {

    public Pizza createPizza(String type) {
        Pizza pizza = null;

        if (type.equals("cheese")) {
            pizza = new CheesePizza();
        } else if (type.equals("pepperoni")) {
            pizza = new PepperoniPizza();
        } else if (type.equals("clam")) {
            pizza = new ClamPizza();
        } else if (type.equals("veggie")) {
            pizza = new VeggiePizza();
        }
        return pizza;
    }
}
