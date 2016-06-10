package wssimulator;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Main class for launching wssimulator in standalone mode.
 */
public class Bootstrap {

    public static void main(String[] args) {
        Options options = setupOptions();

        final CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("h")) {
                usage(options);
            }
            if (cmd.hasOption("p")) {
                setPort(cmd.getOptionValue("p"));
            }
            if (cmd.hasOption("y")) {
                loadFileOrScan(cmd.getOptionValue("y"));
            }
            if (cmd.getArgList() != null && cmd.getArgList().size() > 0) {
                //parse each of them to see if they are a valid specification
                loadSimulation(cmd.getArgList());

            }
        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            System.exit(1);
        } catch (NumberFormatException nfe) {
            System.err.println("Port address not valid");
            System.exit(1);
        }

    }

    /**
     * Loads all simulations from a list of strings.
     *
     * @param simulations potential simulations
     */
    private static void loadSimulation(List<String> simulations) {
        for (String simulation : simulations) {
            WSSimulator.addSimulation(simulation);
        }
    }

    /**
     * Scans the directory or YAML files to load into the WSSimulator manager.
     *
     * @param fileOrDirectory the directory to scan for YAML configuration files.
     */
    private static void loadFileOrScan(String fileOrDirectory) {
        File fileOrDirectoryAsFile = new File(fileOrDirectory);
        if (fileOrDirectoryAsFile.exists() && fileOrDirectoryAsFile.isDirectory()) {
            Collection<File> files = FileUtils.listFiles(new File(fileOrDirectory), new String[]{"yaml"}, false);
            WSSimulator.addSimulations(files);
        } else if (fileOrDirectoryAsFile.exists()) {
            WSSimulator.addSimulation(fileOrDirectoryAsFile);
        } else {
            throw new YamlNotValidException("Directory/File could not be found");
        }
    }

    /**
     * Sets up the CLI options to start wssimulator on.
     *
     * @return the options collection.
     */
    @NotNull
    private static Options setupOptions() {
        Options options = new Options();
        Option help = new Option("h", "print this message");
        Option port = new Option("p", true, "Set the HTTP Port to start the server on (1 to 65535");
        Option fileOrDirectory = new Option("y", true, "Reference to a single yaml file or to a directory (which will load all *.yml files within the target directory)\n");
        Option sampleYamlFile = new Option("s", true, "Print out an example YAML file");
        options.addOption(help);
        options.addOption(port);
        options.addOption(fileOrDirectory);
        options.addOption(sampleYamlFile);
        return options;
    }

    /**
     * Sets the port to start the server on.
     *
     * @param port the numerical port
     */
    private static void setPort(String port) {
        WSSimulator.setPort(Integer.valueOf(port));
    }

    /**
     * Defines the usage
     *
     * @param options the help options.
     */
    private static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("wsemaultor <options>", options);
        System.exit(-1);
    }

}
