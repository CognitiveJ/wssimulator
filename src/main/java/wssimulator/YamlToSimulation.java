package wssimulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;


/**
 * Converts valid YAML to a simulation.
 */
public class YamlToSimulation {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(YamlToSimulation.class);
    private File yamlFile;
    private String yamlString;

    public YamlToSimulation(@NotNull File yamlFile) {
        this.yamlFile = yamlFile;
    }

    public YamlToSimulation(@NotNull String yamlString) {
        this.yamlString = yamlString;
    }

    @NotNull
    public WSSimulation simulatorSimulation() {
        if (yamlFile != null)
            try {
                yamlString = FileUtils.readFileToString(yamlFile, Charset.defaultCharset());
            } catch (IOException e) {
                LOG.error(String.format("Could not read yaml file:(%s)", yamlFile), e);
                throw new YamlNotValidException("Cannot read file");
            }
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        WSSimulation simulation;
        try {
            simulation = mapper.readValue(yamlString, WSSimulation.class);
        } catch (Exception e) {
            LOG.error(String.format("Could not parse yaml file:(%s)", yamlFile), e);
            throw new YamlNotValidException(String.format("Could not parse yaml file:(%s)", yamlFile));
        }
        return simulation;

    }
}
