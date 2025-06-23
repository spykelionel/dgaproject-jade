// src/util/ComponentWeights.java
package dga;

import java.util.HashMap;
import java.util.Map;

public class ComponentWeights {
    public static class ComponentInfo {
        public int cost;
        public boolean tempSensitive;
        public boolean loadSensitive;
        public boolean humiditySensitive;

        public ComponentInfo(int cost, boolean temp, boolean load, boolean humidity) {
            this.cost = cost;
            this.tempSensitive = temp;
            this.loadSensitive = load;
            this.humiditySensitive = humidity;
        }
    }

    public static final Map<String, ComponentInfo> weights = new HashMap<>();

    static {
        // Genetic Cluster 1
        weights.put("C5", new ComponentInfo(5, false, true, true));
        weights.put("C11", new ComponentInfo(3, true, false, false));
        weights.put("C21", new ComponentInfo(7, true, true, true));

        // Genetic Cluster 2
        weights.put("C1", new ComponentInfo(4, false, true, false));
        weights.put("C6", new ComponentInfo(4, false, true, true));
        weights.put("C12", new ComponentInfo(5, true, true, false));
        weights.put("C17", new ComponentInfo(6, true, false, false));
        weights.put("C22", new ComponentInfo(6, false, true, true));

        // Genetic Cluster 3
        weights.put("C7", new ComponentInfo(3, true, false, true));
        weights.put("C13", new ComponentInfo(5, false, false, false));
        weights.put("C23", new ComponentInfo(4, true, true, true));

        // Genetic Cluster 4
        weights.put("C2", new ComponentInfo(4, true, true, false));
        weights.put("C8", new ComponentInfo(3, false, true, false));
        weights.put("C14", new ComponentInfo(5, true, false, true));
        weights.put("C18", new ComponentInfo(5, true, true, false));
        weights.put("C24", new ComponentInfo(6, true, true, true));

        // Genetic Cluster 5
        weights.put("C9", new ComponentInfo(4, false, true, false));
        weights.put("C15", new ComponentInfo(5, true, false, true));
        weights.put("C19", new ComponentInfo(4, true, false, false));
        weights.put("C25", new ComponentInfo(6, true, true, false));
    }
}
