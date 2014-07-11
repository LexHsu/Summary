package factorymethod.product;

abstract public class Operation {

    protected double mNumberA;
    protected double mNumberB;
    
    public double getNumberA() {
        return mNumberA;
    }

    public void setNumberA(double numberA) {
        this.mNumberA = numberA;
    }

    public double getNumberB() {
        return mNumberB;
    }

    public void setNumberB(double numberB) {
        this.mNumberB = numberB;
    }

    public abstract double getResult() throws Exception;
}

