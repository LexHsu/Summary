package proxy;

import proxy.subject.Agent;
import proxy.subject.impl.Rentable;

public class Client {

    public static void main(String[] args) {
        Rentable agent = new Agent();
        agent.rent();
    }
}
