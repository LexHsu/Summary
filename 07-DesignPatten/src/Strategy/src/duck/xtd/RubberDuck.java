package duck.xtd;

import duck.Duck;
import fly.impl.FlyNoWay;
import quack.impl.Squeak;

public class RubberDuck extends Duck {
 
    public RubberDuck() {
        mFlyBehavior = new FlyNoWay();
        mQuackBehavior = new Squeak();
    }
    
    @Override
    public void display() {
        System.out.println("I'm a rubber duckie");
    }
}
