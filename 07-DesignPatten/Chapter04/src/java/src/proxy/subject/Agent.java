package proxy.subject;

import proxy.subject.impl.Landlord;
import proxy.subject.impl.Rentable;

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
