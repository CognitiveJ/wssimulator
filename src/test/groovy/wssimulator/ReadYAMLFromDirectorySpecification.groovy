package wssimulator

import org.apache.commons.io.FileUtils
import spock.lang.Specification

/**
 * Tests reading all yaml files from a specific directory.
 */
class ReadYAMLFromDirectorySpecification extends Specification {

    def "Read all simulation files from a directory"() {
        setup:
        def files = FileUtils.listFiles(new File("src/test/resources/dir1"), ["yml"] as String[], false)
        when:
        WSSimulator.addSimulations(files)
        then:
        WSSimulator.loadedSimulationCount() == 2
        cleanup:
        WSSimulator.shutdown()
    }


}
