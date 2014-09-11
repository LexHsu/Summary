package simplefactory.product;

import java.util.ArrayList;

abstract public class Pizza {
    protected String mName;
    protected String mDough;
    protected String mSauce;
    protected ArrayList<String> mToppings = new ArrayList<String>();

    public String getName() {
        return mName;
    }

    public void prepare() {
        System.out.println("Preparing " + mName);
    }

    public void bake() {
        System.out.println("Baking " + mName);
    }

    public void cut() {
        System.out.println("Cutting " + mName);
    }

    public void box() {
        System.out.println("Boxing " + mName);
    }

    public String toString() {
        // code to display pizza name and ingredients
        StringBuffer display = new StringBuffer();
        display.append("---- " + mName + " ----\n");
        display.append(mDough + "\n");
        display.append(mSauce + "\n");
        for (int i = 0; i < mToppings.size(); i++) {
            display.append(mToppings.get(i) + "\n");
        }
        return display.toString();
    }
}

