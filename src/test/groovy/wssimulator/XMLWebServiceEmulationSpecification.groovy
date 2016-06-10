package wssimulator

import io.restassured.http.ContentType
import spock.lang.Ignore
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.core.IsEqual.equalTo

/**
 * Tests reading all yaml files for XML files.
 */
class XMLWebServiceEmulationSpecification extends Specification {

    def "xmlValidationExample echo test"() {
        setup:
        int port = TestUtils.randomPort()
        when:
        WSSimulator.setPort(port)
        WSSimulator.addSimulation(new File(getClass().getResource("/xml/xmlValidationExample.yml").toURI()))
        then:
        given().port(port)
                .contentType(ContentType.XML)
                .body(new File(getClass().getResource("/xml/xmlValidationExample.xml").toURI()))
                .post("/publish").then().assertThat()
                .statusCode(201).and().body(equalTo("<results>ok</results>"))
        cleanup:
        WSSimulator.shutdown()
    }

}
