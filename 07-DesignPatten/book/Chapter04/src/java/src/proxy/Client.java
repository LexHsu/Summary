package proxy;

import proxy.subject.Rentable;
import proxy.subject.impl.Agent;

public class Client {

    public static void main(String[] args) {
        Rentable agent = new Agent();
        agent.rent();
    }
}
