package wssimulator;


import org.jetbrains.annotations.NotNull;
import spark.Spark;
import wssimulator.handler.BaseHandler;
import wssimulator.handler.GenericHandler;
import wssimulator.handler.JSONHandler;
import wssimulator.handler.XMLHandler;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

/**
 * Manages the service raise
 */
public class WSSimulatorServiceManager {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(WSSimulatorServiceManager.class);


    private List<WSSimulation> validSimulations = new ArrayList<>();

    /**
     * Adds and starts a web service simulator simulation
     *
     * @param simulation the simulation to be added
     */
    public void add(@NotNull WSSimulation simulation) {
        WSSimulatorValidation.validate(simulation);
        setupRoute(simulation);
    }

    /**
     * Sets up a new route within spark.
     *
     * @param simulation the simulator simulation to setup.
     */
    private void setupRoute(@NotNull WSSimulation simulation) {

        BaseHandler handler = handler(simulation);
        validSimulations.add(simulation);
        switch (simulation.httpMethod) {
            case get:
                get(simulation.path, handler::processRequest);
                LOG.info("GET {} now listing", simulation.path);
                break;
            case post:
                post(simulation.path, handler::processRequest);
                LOG.info("POST {} now listing", simulation.path);
                break;
            case put:
                put(simulation.path, handler::processRequest);
                LOG.info("PUT {} now listing", simulation.path);
                break;
            case patch:
                patch(simulation.path, handler::processRequest);
                LOG.info("PATCH {} now listing", simulation.path);
                break;
            case delete:
                delete(simulation.path, handler::processRequest);
                LOG.info("DELETE {} now listing", simulation.path);
                break;
            case head:
                head(simulation.path, handler::processRequest);
                LOG.info("HEAD {} now listing", simulation.path);
                break;
        }
    }

    private BaseHandler handler(@NotNull WSSimulation wsSimulation) {
        if ("application/xml".equals(wsSimulation.consumes))
            return new XMLHandler(wsSimulation);
        else if ("application/json".equals(wsSimulation.consumes))
            return new JSONHandler(wsSimulation);

        return new GenericHandler(wsSimulation);
    }

    /**
     * Shuts down the simulator.
     */
    public void shutdown() {
        LOG.info("Shutting down server");
        Spark.stop();
        validSimulations.clear();
    }

    /**
     * Returns the valid simulation count.
     *
     * @return the number of valid specifiation.
     */
    public int validSimulationCount() {
        return validSimulations.size();
    }


}
