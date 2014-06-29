package simplefactory.client;

import simplefactory.factory.SimplePizzaFactory;
import simplefactory.product.Pizza;

public class PizzaStore {
    SimplePizzaFactory mFactory;
 
    public PizzaStore(SimplePizzaFactory factory) { 
        this.mFactory = factory;
    }
 
    public Pizza orderPizza(String type) {
        Pizza pizza;
 
        pizza = mFactory.createPizza(type);
 
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();

        return pizza;
    }

}
