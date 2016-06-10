package wssimulator

import io.restassured.http.ContentType
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.core.IsEqual.equalTo

/**
 * Tests reading all yaml files for XML files.
 */
class JSONWebServiceEmulationSpecification extends Specification {

    def "validate good json"() {
        setup:
        int port = TestUtils.randomPort()
        when:
        WSSimulator.setPort(port)
        WSSimulator.addSimulation(new File(getClass().getResource("/json/json1.yml").toURI()))
        then:
        given().port(port)
                .contentType(ContentType.XML)
                .body(new File(getClass().getResource("/json/json_good.json").toURI()))
                .post("/publish").then().assertThat()
                .statusCode(201).and().body(equalTo("ok"))
        cleanup:
        WSSimulator.shutdown()
    }

    def "validate bad json"() {
        setup:
        int port = TestUtils.randomPort()
        when:
        WSSimulator.setPort(port)
        WSSimulator.addSimulation(new File(getClass().getResource("/json/json1.yml").toURI()))
        then:
        given().port(port)
                .contentType(ContentType.XML)
                .body(new File(getClass().getResource("/json/json_bad.json").toURI()))
                .post("/publish").then().assertThat()
                .statusCode(400)
        cleanup:
        WSSimulator.shutdown()
    }

}
