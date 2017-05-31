package wssimulator

import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static io.restassured.RestAssured.given
import static org.hamcrest.core.IsEqual.equalTo
import static wssimulator.scanner.SimulationFilters.*
import static wssimulator.scanner.SimulationScanner.classPathScanner

class SimpleSimulatorSpecification extends Specification {

    def "Simple simulator Test"() {
        setup:
        int port = TestUtils.randomPort()
        def simulator = WSSimulator.simulator(port)
        when:
        simulator.addSimulations(classPathScanner(filters(byPackagePrefix("simple"),
                byNamespace("test.simple"))))
        then:
        given().port(port).get("/hello").then().assertThat()
                .statusCode(200).and().body(equalTo("hello world"))
        given().port(port).get("/hello").then().assertThat()
                .statusCode(200).and().body(equalTo("hello world"))
        simulator.loadedSimulationCount() == 1
        simulator.findSimulation("/hello", HttpMethod.get).wsSimulationContext.callCount() == 2
        cleanup:
        simulator.shutdown()
    }

    def "Simple simulator Test using file: simple.yml with countdown hatch"() {
        setup:
        int port = TestUtils.randomPort()
        def simulator = WSSimulator.simulator(port)
        when:
        simulator.addSimulations(classPathScanner(filters(byPackagePrefix("simple"),
                byNamespace("test.simple"))))
        then:
        new Timer().runAfter(10000) {
            given().port(port).get("/hello").then().assertThat()
                    .statusCode(200).and().body(equalTo("hello world"))
        }

        def simulation = simulator.findSimulation("/hello", HttpMethod.get).wsSimulationContext
        simulation.blockUntilCalled(12, TimeUnit.SECONDS)
        simulator.loadedSimulationCount() == 1
        simulation.callCount() == 1
        cleanup:
        simulator.shutdown()
    }
}

