package dga;

import jade.core.Agent;

public class HelloAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + ": Hello! Agent started.");
    }
}
