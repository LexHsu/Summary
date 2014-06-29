package simplefactory.product.sub;

import simplefactory.product.Pizza;

public class CheesePizza extends Pizza {
    public CheesePizza() {
        mName = "Cheese Pizza";
        mDough = "Regular Crust";
        mSauce = "Marinara Pizza Sauce";
        mToppings.add("Fresh Mozzarella");
        mToppings.add("Parmesan");
    }
}
