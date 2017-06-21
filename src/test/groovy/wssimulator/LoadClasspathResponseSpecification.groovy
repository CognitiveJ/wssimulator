package wssimulator

import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static io.restassured.RestAssured.given
import static org.hamcrest.core.IsEqual.equalTo
import static wssimulator.scanner.SimulationFilters.*
import static wssimulator.scanner.SimulationScanner.classPathScanner

class LoadClasspathResponseSpecification extends Specification {

    def "load file test"() {
        setup:
        int port = TestUtils.randomPort()
        def simulator = WSSimulator.simulator(port)
        when:
        simulator.addSimulations(classPathScanner(filters(byPackagePrefix("file"),
                byNamespace("test.file.load.simple"))))
        then:
        given().port(port).get("/hello").then().assertThat()
                .statusCode(200).and().body(equalTo("abc123"))
        simulator.loadedSimulationCount() == 1
        simulator.findSimulation("/hello", HttpMethod.get).wsSimulationContext.callCount() == 1
        cleanup:
        simulator.shutdown()
    }

    def "load file test relative"() {
        setup:
        int port = TestUtils.randomPort()
        def simulator = WSSimulator.simulator(port)
        when:
        simulator.addSimulations(classPathScanner(filters(byPackagePrefix("file"),
                byNamespace("test.file.load.relative"))))
        then:
        given().port(port).get("/hello").then().assertThat()
                .statusCode(200).and().body(equalTo("abc123"))
        simulator.loadedSimulationCount() == 1
        simulator.findSimulation("/hello", HttpMethod.get).wsSimulationContext.callCount() == 1
        cleanup:
        simulator.shutdown()
    }

    def "load file test not found"() {
        setup:
        int port = TestUtils.randomPort()
        def simulator = WSSimulator.simulator(port)
        when:
        simulator.addSimulations(classPathScanner(filters(byPackagePrefix("file"),
                byNamespace("test.file.load.not.found"))))
        then:
        given().port(port).get("/hello").then().assertThat()
                .statusCode(200).and().body(equalTo(""))
        simulator.loadedSimulationCount() == 1
        simulator.findSimulation("/hello", HttpMethod.get).wsSimulationContext.callCount() == 1
        cleanup:
        simulator.shutdown()
    }

}

