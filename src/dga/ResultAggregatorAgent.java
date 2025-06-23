
package dga;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;

import java.util.HashMap;
import java.util.Map;

public class ResultAggregatorAgent extends Agent {
    private Map<String, String> results = new HashMap<>();

    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getPerformative() == ACLMessage.INFORM) {
                    String[] parts = msg.getContent().split(",");
                    if (parts.length == 3) {
                        String sampleId = parts[0];
                        String method = parts[1];
                        String label = parts[2];

                        results.put(sampleId, label);
                        System.out.println(getLocalName() + ": Received label [" + label + "] for sample [" + sampleId + "] via method [" + method + "]");
                    }
                } else {
                    block();
                }
            }
        });
    }
}
