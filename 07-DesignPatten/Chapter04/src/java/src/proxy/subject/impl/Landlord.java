package proxy.subject.impl;


public class Landlord implements Rentable {

    @Override
    public void rent() {
        System.out.println("i have a room for rent.");
    }

}
