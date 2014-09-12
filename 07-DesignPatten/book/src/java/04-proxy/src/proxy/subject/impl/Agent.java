package proxy.subject.impl;

import proxy.subject.Rentable;


public class Agent implements Rentable {

    private Landlord mLandlord;
    
    @Override
    public void rent() {
        if (mLandlord == null) {
            mLandlord = new Landlord();
        }
        
        mLandlord.rent();
        // do other things
    }

}
