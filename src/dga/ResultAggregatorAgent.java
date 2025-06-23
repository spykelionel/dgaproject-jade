package dga;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;

import java.util.*;
import java.util.stream.Collectors;

public class ResultAggregatorAgent extends Agent {
    private final Map<String, Map<String, String>> results = new HashMap<>();

    // Mock environment data
    private final Map<String, Integer> tempMap = Map.of(
        "1", 85, "2", 78, "3", 90, "4", 75, "5", 92, "6", 65, "7", 70, "8", 69, "9", 100, "10", 88
    );

    private final Map<String, Integer> loadMap = Map.of(
        "1", 420, "2", 430, "3", 480, "4", 300, "5", 450, "6", 390, "7", 405, "8", 395, "9", 470, "10", 410
    );

    private final Map<String, Integer> humidityMap = Map.of(
        "1", 85, "2", 90, "3", 75, "4", 68, "5", 95, "6", 72, "7", 86, "8", 88, "9", 91, "10", 92
    );

    private final List<Set<String>> clusters = List.of(
        Set.of("C5", "C11", "C21"),
        Set.of("C1", "C6", "C12", "C17", "C22"),
        Set.of("C7", "C13", "C23"),
        Set.of("C2", "C8", "C14", "C18", "C24"),
        Set.of("C9", "C15", "C19", "C25")
    );

    protected void setup() {
        System.out.println(getLocalName() + ": Aggregator ready.");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getPerformative() == ACLMessage.INFORM) {
                    String[] parts = msg.getContent().split(",");
                    if (parts.length == 3) {
                        String sampleId = parts[0];
                        String method = parts[1];
                        String label = parts[2];

                        results.computeIfAbsent(sampleId, k -> new HashMap<>()).put(method, label);

                        if (results.get(sampleId).size() == 6) {
                            System.out.println("\nTransformer " + sampleId + ":");

                            Map<String, String> sampleResult = results.get(sampleId);
                            sampleResult.entrySet().stream()
                                .sorted(Map.Entry.comparingByKey())
                                .forEach(entry -> System.out.println("- " + entry.getKey() + " -> " + entry.getValue()));

                            evaluateMI(sampleId, sampleResult);
                            evaluateCost(sampleId, sampleResult);
                        }
                    }
                } else {
                    block();
                }
            }
        });
    }

    private void evaluateMI(String sampleId, Map<String, String> sampleResult) {
        String miLabel = sampleResult.get("MI");
        boolean foundInCluster = clusters.stream().anyMatch(cluster -> cluster.contains(miLabel));
        String status = foundInCluster ? "GOOD: MI aligns with genetic pattern" : "BAD: MI NOT in genetic cluster";

        int temp = tempMap.getOrDefault(sampleId, 70);
        int load = loadMap.getOrDefault(sampleId, 350);
        int humidity = humidityMap.getOrDefault(sampleId, 50);
        boolean highStress = (temp > 80 || load > 400 || humidity > 80);

        System.out.println("-> MI verdict: " + status);
        System.out.println("-> Environmental Stress: " + (highStress ? "HIGH" : "NORMAL"));
    }

    private void evaluateCost(String sampleId, Map<String, String> sampleResult) {
        int budget = 20;
        int totalCost = 0;
        List<String> overSensitive = new ArrayList<>();

        int temp = tempMap.getOrDefault(sampleId, 70);
        int load = loadMap.getOrDefault(sampleId, 350);
        int humidity = humidityMap.getOrDefault(sampleId, 50);

        for (String label : sampleResult.values()) {
            if (label.startsWith("UNMAPPED_")) continue;

            ComponentWeights.ComponentInfo info = ComponentWeights.weights.get(label);
            if (info != null) {
                totalCost += info.cost;
                if ((temp > 80 && info.tempSensitive) ||
                    (load > 400 && info.loadSensitive) ||
                    (humidity > 80 && info.humiditySensitive)) {
                    overSensitive.add(label);
                }
            }
        }

        System.out.println("-> Component Budget: " + totalCost + "/20 " +
                (totalCost > budget ? "BAD: OVER BUDGET" : "GOOD: within budget"));

        if (totalCost > budget) {
            System.out.println("-> CAUTION: Maintenance action may be required!");
        }

        if (!overSensitive.isEmpty()) {
            String list = overSensitive.stream().collect(Collectors.joining(", "));
            System.out.println("-> CAUTION: Stress-sensitive components: " + list);
        }
    }
}
