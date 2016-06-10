package wssimulator

import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.core.IsEqual.equalTo

/**
 *
 */
class WSSimulatorSettingsSpecification extends Specification {

    def random = new Random()

    def "Test a port change"() {
        int allocatedPort = 1024 + random.nextInt(64000)
        when:
        WSSimulator.setPort(allocatedPort)
        WSSimulator.addSimulation(new File(getClass().getResource("/simple2.yml").toURI()))
        then:
        given().port(allocatedPort).get("/hello/tester").then().assertThat()
                .statusCode(201).and().body(equalTo("Hello World tester"))
        cleanup:
        WSSimulator.shutdown()
    }

}
