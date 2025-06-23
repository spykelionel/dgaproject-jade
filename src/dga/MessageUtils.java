
package dga;

/**
 * Utility class to wrap and parse JADE message contents.
 */
public class MessageUtils {

    public static String encodeFault(String sampleId, String faultCode) {
        return sampleId + "," + faultCode;
    }

    public static String encodeLabel(String sampleId, String method, String label) {
        return sampleId + "," + method + "," + label;
    }

    public static String[] decode(String messageContent) {
        return messageContent.split(",");
    }
}
