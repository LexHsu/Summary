package duck.xtd;

import duck.Duck;
import quack.impl.Quack;
import fly.impl.FlyWithWings;

public class MallardDuck extends Duck {
 
    public MallardDuck() {
        mQuackBehavior = new Quack();
        mFlyBehavior = new FlyWithWings();
    }
 
    @Override
    public void display() {
        System.out.println("I'm a real Mallard duck");
    }
}
