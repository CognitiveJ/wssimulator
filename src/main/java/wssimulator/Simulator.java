package wssimulator;


import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class Simulator {
    private WSSimulatorHandlerService wsSimulatorHandlerService = new WSSimulatorHandlerService();
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Simulator.class);

    public void setPort(int port) {
        throwIf(port <= 0 && port > 65535,
                new RuntimeException(String.format("assignedPort no in bounds bounds (1-65535) %s", port)));
        wsSimulatorHandlerService.assignedPort(port);
    }

    /**
     * adds a collection of simulations to the simulator.
     *
     * @param simulations the collection of files to add to the emaulator
     * @return this
     */
    @NotNull
    public Simulator addSimulations(Collection<WSSimulation> simulations) {
        simulations.forEach(echoSimulationAsYaml -> {
            try {
                wsSimulatorHandlerService.add(echoSimulationAsYaml);
                LOG.info("loaded YAML Simulation{}", echoSimulationAsYaml);
            } catch (YamlNotValidException e) {
                LOG.warn("Failed to load {}", echoSimulationAsYaml);
            }
        });
        return this;
    }

    /**
     * Returns the count of all simulations that are loaded within the simulator.
     *
     * @return the number of validate simulations.
     */
    public int loadedSimulationCount() {
        return wsSimulatorHandlerService.validSimulationCount();
    }

    /**
     * Return the simulations of a given namespace.
     *
     * @param namespace the namespace to search against.
     * @return A Collection of loaded simulations.
     */
    public Collection<WSSimulation> findSimulationsNamespace(@NotNull String namespace) {
        return wsSimulatorHandlerService.findSimulationsNamespace(namespace);
    }

    /**
     * Return simulation path based on its logical path
     *
     * @param path       the path that the simulation
     * @param httpMethod the httpmethod of the specification
     * @return the found simulation
     * @throws SimulationNotFoundException when simulation isn't found
     */
    public WSSimulation findSimulation(@NotNull String path, @NotNull HttpMethod httpMethod) {
        int simulationId = findSimulationId(path, httpMethod);
        return wsSimulatorHandlerService.getWSSimulation(simulationId);
    }

    /**
     * Return the ID of a simulation path based on its logical path
     *
     * @param path       the path that the simulation
     * @param httpMethod the httpmethod of the specification
     * @return the id or -1 if not loaded.
     */
    public int findSimulationId(@NotNull String path, @NotNull HttpMethod httpMethod) {
        return wsSimulatorHandlerService.findSimulationIdByPath(path, httpMethod);
    }



    /**
     * Return the Simulation of a simulation path based on its name.
     * @param name       the name of the simulation
     * @return the id or -1 if not loaded.
     */
    public WSSimulation findSimulationByName(@NotNull String name) {
        return wsSimulatorHandlerService.findSimulationByName(name);
    }


    /**
     * Shuts down the simulator
     */
    public void shutdown() {
        LOG.info("Shutting down server");
        wsSimulatorHandlerService.shutdownAll();
    }

    private static void throwIf(boolean toThrow, RuntimeException toBeThrown) {
        if (toThrow)
            throw toBeThrown;
    }

}
