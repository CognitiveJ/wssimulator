package wssimulator.handler;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wssimulator.WSSimulation;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static wssimulator.WSSimulatorValidation.validateXml;

/**
 * Handles XML based payloads.
 */
public class XMLHandler extends BaseHandler {

    public XMLHandler(@NotNull WSSimulation WSSimulation) {
        super(WSSimulation);
    }

    @Override
    protected boolean validate(@NotNull WSSimulation WSSimulation, @Nullable String body) {
        if (isNotEmpty(WSSimulation.requestStructure) && isNotEmpty(body))
            return validateXml(WSSimulation.requestStructure, body);
        return true;

    }

}
