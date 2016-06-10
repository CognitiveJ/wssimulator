package wssimulator;


import org.jetbrains.annotations.NotNull;
import spark.Spark;

import java.io.File;
import java.util.Collection;

/**
 * Base simulator for web services.
 */
public final class WSSimulator {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(WSSimulator.class);
    private static WSSimulatorServiceManager wsSimulatorServiceManager = new WSSimulatorServiceManager();

    /**
     * Apply a simulation to be emulated
     *
     * @param yamlString YAML as a String
     */
    public static void addSimulation(@NotNull String yamlString) {
        YamlToSimulation yamlToSimulation = new YamlToSimulation(yamlString);
        wsSimulatorServiceManager.add(yamlToSimulation.simulatorSimulation());
    }

    /**
     * Apply a simulation to be emulated
     *
     * @param WSSimulation the WSSimulation
     */
    public static void addSimulation(@NotNull WSSimulation WSSimulation) {
        wsSimulatorServiceManager.add(WSSimulation);
    }


    /**
     * Adds a simulation from a YAML file
     *
     * @param echoSimulationAsYaml the yaml file
     */
    public static void addSimulation(@NotNull File echoSimulationAsYaml) {
        YamlToSimulation yamlToSimulation = new YamlToSimulation(echoSimulationAsYaml);
        addSimulation(yamlToSimulation.simulatorSimulation());
    }

    /**
     * Shuts down the simulator
     */
    public static void shutdown() {
        wsSimulatorServiceManager.shutdown();
    }

    /**
     * Sets the port to start the http server.
     *
     * @param port the port number
     */
    public static void setPort(int port) {
        Spark.port(port);
    }


    /**
     * adds a collection of simulations to the simulator.
     *
     * @param simulations the collection of files to add to the emaulator
     */
    public static void addSimulations(Collection<File> simulations) {

        simulations.forEach(echoSimulationAsYaml -> {
            try {
                WSSimulator.addSimulation(echoSimulationAsYaml);
                LOG.info("loaded YAML Simulation{}", echoSimulationAsYaml);
            } catch (YamlNotValidException e) {
                LOG.warn("Failed to load {}", echoSimulationAsYaml);
            }
        });
    }

    /**
     * Returns the count of all simulations that are loaded within the simulator.
     *
     * @return the number of validate simulations.
     */
    public static int loadedSimulationCount() {
        return wsSimulatorServiceManager.validSimulationCount();
    }
}
