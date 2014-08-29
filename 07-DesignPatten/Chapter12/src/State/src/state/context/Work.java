package state.context;

import state.state.State;
import state.state.impl.ForenoonState;

public class Work {
    private int hour;
    private boolean finish = false;
    private State state;

    public Work() {
        state = new ForenoonState();
    }

    public void writeProgram() {
        state.writeProgram(this);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
