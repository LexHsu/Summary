package duck.xtd;

import duck.Duck;
import fly.impl.FlyNoWay;
import quack.impl.MuteQuack;

public class DecoyDuck extends Duck {
    public DecoyDuck() {
        setFlyBehavior(new FlyNoWay());
        setQuackBehavior(new MuteQuack());
    }
    
    @Override
    public void display() {
        System.out.println("I'm a duck Decoy");
    }
}
