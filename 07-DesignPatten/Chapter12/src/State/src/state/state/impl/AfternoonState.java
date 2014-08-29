package state.state.impl;

import state.context.Work;
import state.state.State;

//下午工作状态
public class AfternoonState implements State {
    public void writeProgram(Work work) {
        if (work.getHour() < 17) {
            System.out.println("Time：" + work.getHour() + ", Afternoon State.");
        } else {
            work.setState(new RestState());
            work.writeProgram();
        }
    }
}
