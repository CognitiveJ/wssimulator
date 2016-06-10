package wssimulator;


import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a full simulation for the wssimulator service
 */
public class WSSimulation {

    /**
     * The paths corresponding httpmethod
     *
     * @see HttpMethod
     */
    public HttpMethod httpMethod = HttpMethod.get;

    /**
     * The path of this service
     */
    public String path;

    /**
     * Mediatype the service will consume.
     */
    public String consumes = "text/plain";

    /**
     * Default Response
     */
    public String response;

    /**
     * Validate the structure of the request.
     */
    public String requestStructure;

    /**
     * sets the successful response code to send back to the server
     */
    public int successResponseCode = 201;

    /**
     * sets the response code when the data is not valid;
     */
    public int badRequestResponseCode = 400;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
