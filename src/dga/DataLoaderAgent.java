
package dga;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.OneShotBehaviour;

import java.io.File;
import java.util.Scanner;

public class DataLoaderAgent extends Agent {

    private static final String[] METHODS = {"KGM", "IEC", "RRM", "DRM", "DTM", "MI"};

    @Override
    protected void setup() {
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                try (Scanner sc = new Scanner(new File("data/fault_codes.csv"))) {
                    // Read header
                    String headerLine = sc.nextLine();
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();
                        String[] parts = line.split(",");
                        if (parts.length < METHODS.length + 1) continue;

                        String sampleId = parts[0].trim();
                        for (int i = 0; i < METHODS.length; i++) {
                            String method = METHODS[i];
                            String faultCode = parts[i + 1].trim();
                            if (!faultCode.isEmpty()) {
                                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                                msg.setContent(sampleId + "," + faultCode);
                                msg.addReceiver(new AID("mapper-" + method.toLowerCase(), AID.ISLOCALNAME));
                                send(msg);
                            }
                        }
                    }
                    System.out.println(getLocalName() + ": Finished dispatching faults.");
                } catch (Exception e) {
                    System.err.println(getLocalName() + ": Error reading fault_codes.csv - " + e.getMessage());
                }
            }
        });
    }
}
