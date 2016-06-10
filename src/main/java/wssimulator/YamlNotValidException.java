package wssimulator;

/**
 * Thrown when the YAML is not well formed or missing.
 */
public class YamlNotValidException extends RuntimeException {

    public YamlNotValidException(String message) {
        super(message);
    }
}
