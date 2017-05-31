package wssimulator

import org.apache.commons.lang3.time.StopWatch
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.core.IsEqual.equalTo
import static wssimulator.scanner.SimulationFilters.*
import static wssimulator.scanner.SimulationScanner.classPathScanner

class LatencyTestSpecification extends Specification {


    def "Simple latency simulator Test"() {
        setup:
        int port = TestUtils.randomPort()
        def simulator = WSSimulator.simulator(port)
        when:
        simulator.addSimulations(classPathScanner(filters(byPackagePrefix("latency"),
                byNamespace("test.latency.simple"))))
        StopWatch stopWatch = new StopWatch()
        stopWatch.start()
        then:
        given().port(port).get("/hello").then().assertThat()
                .statusCode(200)
                .and()
                .body(equalTo("hello world"))
        stopWatch.getTime() > 5000
        simulator.loadedSimulationCount() == 1
        simulator.findSimulation("/hello", HttpMethod.get).wsSimulationContext.callCount() == 1
        cleanup:
        simulator.shutdown()
    }

    def "bounded latency simulator Test"() {
        setup:
        int port = TestUtils.randomPort()
        def simulator = WSSimulator.simulator(port)
        when:
        simulator.addSimulations(classPathScanner(filters(byPackagePrefix("latency"),
                byNamespace("test.latency.bounded"))))
        StopWatch stopWatch = new StopWatch()
        stopWatch.start()
        then:
        given().port(port).get("/hello").then().assertThat()
                .statusCode(200).and().body(equalTo("hello world"))
        stopWatch.getTime() > 5000 && stopWatch.getTime() < 8000
        simulator.loadedSimulationCount() == 1
        simulator.findSimulation("/hello", HttpMethod.get).wsSimulationContext.callCount() == 1
        cleanup:
        simulator.shutdown()
    }
}
