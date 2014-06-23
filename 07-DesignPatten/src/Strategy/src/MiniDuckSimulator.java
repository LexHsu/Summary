
import duck.xtd.DecoyDuck;
import duck.xtd.MallardDuck;
import duck.xtd.ModelDuck;
import duck.xtd.RubberDuck;
import fly.impl.FlyRocketPowered;

public class MiniDuckSimulator {
 
    public static void main(String[] args) {
 
        MallardDuck mallard = new MallardDuck();
        RubberDuck rubberDuckie = new RubberDuck();
        DecoyDuck decoy = new DecoyDuck();
 
        ModelDuck model = new ModelDuck();

        mallard.performQuack();
        rubberDuckie.performQuack();
        decoy.performQuack();
   
        model.performFly();
        model.setFlyBehavior(new FlyRocketPowered());
        model.performFly();
    }
}
