
package dga;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DGAMapperAgent extends Agent {
    private String method;
    private Map<String, String> mappings = new HashMap<>();

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            method = (String) args[0];
        } else {
            method = "UNKNOWN";
            System.err.println(getLocalName() + ": No method argument provided!");
        }

        loadMappings();

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getPerformative() == ACLMessage.INFORM) {
                    String[] contentParts = msg.getContent().split(",");
                    if (contentParts.length == 2) {
                        String sampleId = contentParts[0];
                        String faultCode = contentParts[1];
                        String key = method + "_" + faultCode;
                        String label = mappings.getOrDefault(key, "UNMAPPED_" + faultCode);

                        ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                        response.addReceiver(new AID("aggregator", AID.ISLOCALNAME));
                        response.setContent(sampleId + "," + method + "," + label);
                        send(response);
                    }
                } else {
                    block();
                }
            }
        });
    }

    private void loadMappings() {
        try (Scanner sc = new Scanner(new File("data/mappings.csv"))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split("=");
                if (parts.length == 2) {
                    mappings.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (Exception e) {
            System.err.println(getLocalName() + ": Error loading mappings - " + e.getMessage());
        }
    }
}
