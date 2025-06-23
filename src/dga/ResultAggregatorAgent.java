package dga;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

public class ResultAggregatorAgent extends Agent {
    private Map<String, String> results = new HashMap<>();

    @Override
    protected void setup() {
       addBehaviour(new CyclicBehaviour() {
    // Map<sampleId, Map<method, label>>
    private final Map<String, Map<String, String>> formattedResults = new HashMap<>();

    public void action() {
        ACLMessage msg = receive();
        if (msg != null && msg.getPerformative() == ACLMessage.INFORM) {
            String[] parts = msg.getContent().split(",");
            if (parts.length == 3) {
                String sampleId = parts[0];
                String method = parts[1];
                String label = parts[2];

                // Store result in nested map
                formattedResults
                    .computeIfAbsent(sampleId, k -> new LinkedHashMap<>())
                    .put(method, label);

                // Check if we've received all 6 method results for this sample
                if (formattedResults.get(sampleId).size() == 6) {
                    System.out.println("Transformer " + sampleId + ":");
                    Map<String, String> labels = formattedResults.get(sampleId);

                    // Order of methods
                    String[] methods = {"KGM", "IEC", "RRM", "DRM", "DTM", "MI"};
                    for (String m : methods) {
                        String lbl = labels.getOrDefault(m, "UNMAPPED");
                        System.out.println("- " + m + " -> " + lbl);
                    }
                    System.out.println(); // Blank line after each sample
                }
            }
        } else {
            block();
        }
    }
});

    }
}
