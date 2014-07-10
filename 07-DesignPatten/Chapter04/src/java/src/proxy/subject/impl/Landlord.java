package proxy.subject.impl;

import proxy.subject.Rentable;


public class Landlord implements Rentable {

    @Override
    public void rent() {
        System.out.println("i have a room for rent.");
    }

}
