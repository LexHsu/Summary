package duck;

import quack.QuackBehavior;
import fly.FlyBehavior;

public abstract class Duck {
    protected FlyBehavior mFlyBehavior;
    protected QuackBehavior mQuackBehavior;
 
    public Duck() {
    }
 
    public void setFlyBehavior (FlyBehavior fb) {
        mFlyBehavior = fb;
    }
 
    public void setQuackBehavior(QuackBehavior qb) {
        mQuackBehavior = qb;
    }
 
    public abstract void display();
 
    public void performFly() {
        mFlyBehavior.fly();
    }
 
    public void performQuack() {
        mQuackBehavior.quack();
    }
 
    public void swim() {
        System.out.println("All ducks float, even decoys!");
    }
}
