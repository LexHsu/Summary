package simplefactory.product.sub;

import simplefactory.product.Pizza;

public class PepperoniPizza extends Pizza {
    public PepperoniPizza() {
        mName = "Pepperoni Pizza";
        mDough = "Crust";
        mSauce = "Marinara sauce";
        mToppings.add("Sliced Pepperoni");
        mToppings.add("Sliced Onion");
        mToppings.add("Grated parmesan cheese");
    }
}
