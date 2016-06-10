package wssimulator

import spock.lang.Specification

class SadPathTestSpecification extends Specification {

    def "test passing YAML in as a string"() {
        when:
        WSSimulator.addSimulation("not yaml")
        then:
        thrown YamlNotValidException
        cleanup:
        WSSimulator.shutdown();
    }
}
