package example;

import wssimulator.WSSimulator;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Example showing how to create a simulated soap service
 */
public class SOAPExample {
    public static void main(String[] args) throws URISyntaxException {
        WSSimulator.addSimulation(new File(SOAPExample.class.getResource("/soap/wsdl.yml").toURI()));
        WSSimulator.addSimulation(new File(SOAPExample.class.getResource("/soap/endpoint.yml").toURI()));
    }

}
