package wssimulator.handler;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wssimulator.WSSimulation;

/**
 * Handles all other media types
 */
public class GenericHandler extends BaseHandler {

    public GenericHandler(@NotNull WSSimulation WSSimulation) {
        super(WSSimulation);
    }

    @Override
    protected boolean validate(@NotNull WSSimulation WSSimulation, @Nullable String body) {
        return true;
    }
}
