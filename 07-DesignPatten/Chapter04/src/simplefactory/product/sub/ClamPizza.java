package simplefactory.product.sub;

import simplefactory.product.Pizza;

public class ClamPizza extends Pizza {
    public ClamPizza() {
        mName = "Clam Pizza";
        mDough = "Thin crust";
        mSauce = "White garlic sauce";
        mToppings.add("Clams");
        mToppings.add("Grated parmesan cheese");
    }
}
