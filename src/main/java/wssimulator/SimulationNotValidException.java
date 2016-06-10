package wssimulator;

import org.jetbrains.annotations.NotNull;

/**
 * thrown when the simulation passed in cannot be parsed
 */
public class SimulationNotValidException extends RuntimeException {

    private final WSSimulation WSSimulation;

    public SimulationNotValidException(@NotNull WSSimulation WSSimulation) {
        this.WSSimulation = WSSimulation;
    }

    @NotNull
    public WSSimulation simulation() {
        return WSSimulation;
    }
}
