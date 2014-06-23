package duck.xtd;

import duck.Duck;
import fly.impl.FlyNoWay;
import quack.impl.Quack;

public class ModelDuck extends Duck {
    public ModelDuck() {
        mFlyBehavior = new FlyNoWay();
        mQuackBehavior = new Quack();
    }

    @Override
    public void display() {
        System.out.println("I'm a model duck");
    }
}
