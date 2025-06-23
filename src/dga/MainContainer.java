package dga;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class MainContainer {
    public static void main(String[] args) throws Exception {
        Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        AgentContainer mainContainer = rt.createMainContainer(profile);

        AgentController helloAgent = mainContainer.createNewAgent("hello", HelloAgent.class.getName(), null);
        helloAgent.start();
    }
}
