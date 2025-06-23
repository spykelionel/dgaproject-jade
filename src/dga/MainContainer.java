
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

            // Launch aggregator first
            container.createNewAgent("aggregator", ResultAggregatorAgent.class.getName(), null).start();

            // Launch mapper agents for each method
            container.createNewAgent("mapper-kgm", DGAMapperAgent.class.getName(), new Object[]{"KGM"}).start();
            container.createNewAgent("mapper-iec", DGAMapperAgent.class.getName(), new Object[]{"IEC"}).start();
            container.createNewAgent("mapper-rrm", DGAMapperAgent.class.getName(), new Object[]{"RRM"}).start();
            container.createNewAgent("mapper-drm", DGAMapperAgent.class.getName(), new Object[]{"DRM"}).start();
            container.createNewAgent("mapper-dtm", DGAMapperAgent.class.getName(), new Object[]{"DTM"}).start();
            container.createNewAgent("mapper-mi", DGAMapperAgent.class.getName(), new Object[]{"MI"}).start();

            // Launch DataLoader
            container.createNewAgent("data-loader", DataLoaderAgent.class.getName(), null).start();

            System.out.println("✅ All agents launched successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
