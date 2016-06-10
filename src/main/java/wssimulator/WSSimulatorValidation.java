package wssimulator;


import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Utility class to validate datasets within WSSimulator
 */
public class WSSimulatorValidation {

    /**
     * Validates the simulation.
     *
     * @param simulation the simulation to be validated.
     */
    public static void validate(@NotNull WSSimulation simulation) {
        if (StringUtils.isEmpty(simulation.path))
            throw new SimulationNotValidException(simulation);
    }

    /**
     * Uses {@link javax.xml.validation.Validator} to validate XML against XSD
     *
     * @param xsd the schema
     * @param xml the xml to be validated.
     * @return true if the xml matches the xsd
     */
    public static boolean validateXml(@NotNull String xsd, @NotNull String xml) {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(IOUtils.toInputStream(xsd, Charset.defaultCharset())));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(IOUtils.toInputStream(xml, Charset.defaultCharset())));
        } catch (IOException | SAXException ignored) {
            return false;
        }
        return true;
    }

    /**
     * Uses {@link JsonSchema} to validate JSON against A JSON Schema File
     *
     * @param jsonSchema the schema
     * @param json       the json to be validated.
     * @return true if the json matches the json schema
     * @see <a href="https://github.com/fge/json-schema-validator">JSON Schema Validator</a>
     */
    public static boolean validateJSON(@NotNull String jsonSchema, @NotNull String json) {
        try {
            JsonSchema schema = JsonSchemaFactory.byDefault().getJsonSchema(JsonLoader.fromString(jsonSchema));
            JsonNode jsonToBeValidated = JsonLoader.fromString(json);
            return schema.validInstance(jsonToBeValidated);
        } catch (IOException | ProcessingException ignored) {
        }
        return false;
    }
}
