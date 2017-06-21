package wssimulator

import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static io.restassured.RestAssured.given
import static org.hamcrest.core.IsEqual.equalTo
import static wssimulator.scanner.SimulationFilters.*
import static wssimulator.scanner.SimulationScanner.classPathScanner

class PrioritySimulatorSpecification extends Specification {

    def "Simple Priority Test"() {
        setup:
        int port = TestUtils.randomPort()
        def simulator = WSSimulator.simulator(port)
        when:
        simulator.addSimulations(classPathScanner(filters(byPackagePrefix("priority"),
                byNamespace("test.priority"))))
        then:
        given().port(port).get("/priority").then().assertThat()
                .statusCode(200).and().body(equalTo("pr1"))
        simulator.loadedSimulationCount() == 4
        simulator.findSimulation("/priority", HttpMethod.get).wsSimulationContext.callCount() == 1
        cleanup:
        simulator.shutdown()
    }


}

