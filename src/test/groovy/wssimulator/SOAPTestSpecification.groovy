package wssimulator

import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.core.IsEqual.equalTo

class SOAPTestSpecification extends Specification {

    def "Simple simulator Test for SOAP services"() {
        given:
        WSSimulator.addSimulation(new File(getClass().getResource("/soap/wsdl.yml").toURI()))
        WSSimulator.addSimulation(new File(getClass().getResource("/soap/endpoint.yml").toURI()))
        expect:
        given().port(4567).get("/currency.wsdl").then().assertThat()
                .statusCode(201)
        given().port(4567)
                .body(new File(getClass().getResource("/soap/payload.xml").toURI()))
                .post("/CurrencyConvertor.asmx").then().assertThat()
                .statusCode(201)
        cleanup:
        WSSimulator.shutdown();
    }

}
