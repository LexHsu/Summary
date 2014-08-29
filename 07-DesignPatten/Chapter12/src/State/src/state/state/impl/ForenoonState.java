package state.state.impl;

import state.context.Work;
import state.state.State;

public class ForenoonState implements State {
    public void writeProgram(Work work) {
        if (work.getHour() < 12) {
            System.out.println("Time：" + work.getHour() + ", Forenoon State.");
        } else {
            work.setState(new AfternoonState());
            work.writeProgram();
        }
    }
}

