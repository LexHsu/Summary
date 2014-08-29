package state.state.impl;

import state.context.Work;
import state.state.State;

public class RestState implements State {

    @Override
    public void writeProgram(Work work) {
        System.out.println("Time：" + work.getHour() + ", Rest State.");
    }

}