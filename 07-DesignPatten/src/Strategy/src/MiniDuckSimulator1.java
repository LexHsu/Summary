
import duck.Duck;
import duck.xtd.MallardDuck;
import duck.xtd.ModelDuck;
import fly.impl.FlyRocketPowered;

public class MiniDuckSimulator1 {
 
    public static void main(String[] args) {
 
        Duck mallard = new MallardDuck();
        mallard.performQuack();
        mallard.performFly();
   
        Duck model = new ModelDuck();
        model.performFly();
        model.setFlyBehavior(new FlyRocketPowered());
        model.performFly();

    }
}
