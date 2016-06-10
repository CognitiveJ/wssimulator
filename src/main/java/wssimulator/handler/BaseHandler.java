package wssimulator.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spark.Request;
import spark.Response;
import wssimulator.WSSimulation;

import java.util.Map;
import java.util.stream.Collectors;


/**
 * Base handler for handling http requests to the server.
 * This base class has a common features and delegates to specialised classes
 * based on the 'consumes' data element.
 */
public abstract class BaseHandler {


    @NotNull
    private final WSSimulation wsSimulation;

    public BaseHandler(@NotNull WSSimulation wsSimulation) {
        this.wsSimulation = wsSimulation;
    }

    @NotNull
    public final Object processRequest(@NotNull Request request, @NotNull Response response) {
        final Map<String, String> params = buildParameterValues(request);
        if (!validate(wsSimulation, request.body())) {
            response.status(wsSimulation.badRequestResponseCode);
            return "";
        }

        response.status(wsSimulation.successResponseCode);
        if (StringUtils.isNotEmpty(wsSimulation.response))
            return new StrSubstitutor(params).replace(wsSimulation.response);
        return "";
    }

    protected abstract boolean validate(@NotNull WSSimulation WSSimulation,
                                        @Nullable String body);

    private Map<String, String> buildParameterValues(Request request) {
        Map<String, String> params = request
                .params()
                .keySet().stream()
                .collect(Collectors
                        .toMap(p -> String.format("param.%s", p.substring(1)),
                                request::params));
        return params;
    }
}
