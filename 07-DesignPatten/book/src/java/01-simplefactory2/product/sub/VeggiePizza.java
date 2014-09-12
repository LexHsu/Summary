package simplefactory.product.sub;

import simplefactory.product.Pizza;

public class VeggiePizza extends Pizza {
    public VeggiePizza() {
        mName = "Veggie Pizza";
        mDough = "Crust";
        mSauce = "Marinara sauce";
        mToppings.add("Shredded mozzarella");
        mToppings.add("Grated parmesan");
        mToppings.add("Diced onion");
        mToppings.add("Sliced mushrooms");
        mToppings.add("Sliced red pepper");
        mToppings.add("Sliced black olives");
    }
}
