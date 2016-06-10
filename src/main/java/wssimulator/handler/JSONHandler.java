package wssimulator.handler;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wssimulator.WSSimulation;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static wssimulator.WSSimulatorValidation.validateJSON;

/**
 * Handles JSON based payloads.
 */
public class JSONHandler extends BaseHandler {

    public JSONHandler(@NotNull WSSimulation WSSimulation) {
        super(WSSimulation);
    }

    @Override
    protected boolean validate(@NotNull WSSimulation simulatorSimulation, @Nullable String body) {
        if (isNotEmpty(simulatorSimulation.requestStructure) && isNotEmpty(body))
            return validateJSON(simulatorSimulation.requestStructure, body);
        return true;

    }

}
