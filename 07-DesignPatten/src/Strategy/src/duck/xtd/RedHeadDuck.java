package duck.xtd;

import duck.Duck;
import quack.impl.Quack;
import fly.impl.FlyWithWings;

public class RedHeadDuck extends Duck {
 
    public RedHeadDuck() {
        mFlyBehavior = new FlyWithWings();
        mQuackBehavior = new Quack();
    }
 
    @Override
    public void display() {
        System.out.println("I'm a real Red Headed duck");
    }
}
