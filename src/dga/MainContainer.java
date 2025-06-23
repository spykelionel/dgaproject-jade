
package dga;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.AgentContainer;
import jade.core.Runtime;

public class MainContainer {
    public static void main(String[] args) {
        try {
            Runtime rt = Runtime.instance();
            Profile p = new ProfileImpl();
            AgentContainer container = rt.createMainContainer(p);

            // Launch agents
            container.createNewAgent("data-loader", DataLoaderAgent.class.getName(), null).start();
            container.createNewAgent("mapper-1", DGAMapperAgent.class.getName(), new Object[]{"DTC"}).start();
            container.createNewAgent("aggregator", ResultAggregatorAgent.class.getName(), null).start();

            System.out.println("All agents launched.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
