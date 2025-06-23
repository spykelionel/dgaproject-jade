package dga;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;

import java.util.*;

public class ResultAggregatorAgent extends Agent {
    private final Map<String, Map<String, String>> results = new HashMap<>();

    // Simulated temperature and load maps
    private final Map<String, Integer> tempMap = Map.of(
            "1", 85, "2", 78, "3", 90, "4", 75, "5", 92,
            "6", 65, "7", 70, "8", 69, "9", 100, "10", 88
    );

    private final Map<String, Integer> loadMap = Map.of(
            "1", 420, "2", 430, "3", 480, "4", 300, "5", 450,
            "6", 390, "7", 405, "8", 395, "9", 470, "10", 410
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

                            sampleResult.forEach((m, l) ->
                                    System.out.println("- " + m + " -> " + l));

                            String miLabel = sampleResult.get("MI");
                            boolean isUnmapped = miLabel.startsWith("UNMAPPED_");

                            // Analyze genetic alignment
                            boolean foundInCluster = clusters.stream().anyMatch(cluster -> cluster.contains(miLabel));
                            String status = !isUnmapped && foundInCluster ?
                                    "GOOD: MI aligns with genetic pattern" :
                                    "BAD: MI NOT in genetic cluster";

                            if (isUnmapped) {
                                // Try to infer if MI *might* belong based on majority cluster
                                List<String> otherLabels = new ArrayList<>(sampleResult.values());
                                otherLabels.remove(miLabel); // remove MI's label

                                Optional<Set<String>> bestCluster = clusters.stream()
                                        .max(Comparator.comparingInt(cluster ->
                                                (int) otherLabels.stream().filter(cluster::contains).count()));

                                bestCluster.ifPresent(cluster -> {
                                    long count = otherLabels.stream().filter(cluster::contains).count();
                                    if (count >= 3) {
                                        System.out.println("-> MI is unmapped, but other methods suggest genetic cluster: " + cluster);
                                    }
                                });
                            }

                            System.out.println("-> MI verdict: " + status);

                            int temp = tempMap.getOrDefault(sampleId, 70);
                            int load = loadMap.getOrDefault(sampleId, 350);
                            boolean highStress = (temp > 80 || load > 400);

                            System.out.println("-> Environmental Stress: " + (highStress ? "HIGH" : "NORMAL"));
                        }
                    }
                } else {
                    block();
                }
            }
        });
    }
}
