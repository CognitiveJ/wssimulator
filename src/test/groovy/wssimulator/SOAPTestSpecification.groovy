package wssimulator

import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.core.IsEqual.equalTo

class SOAPTestSpecification extends Specification {

    def "Simple simulator Test for SOAP services"() {
        setup:
        int port = TestUtils.randomPort()
        when:
        WSSimulator.setPort(port)
        WSSimulator.addSimulation(new File(getClass().getResource("/soap/wsdl.yml").toURI()))
        WSSimulator.addSimulation(new File(getClass().getResource("/soap/endpoint.yml").toURI()))
        println "WHEN FINISHED!!"
        then:
        println "THEN STARTED!!"

        given().port(port).get("/currency.wsdl").then().assertThat()
                .statusCode(201)
        given().port(port)
                .body(new File(getClass().getResource("/soap/payload.xml").toURI()))
                .post("/CurrencyConvertor.asmx").then().assertThat()
                .statusCode(201)
        println "THEN FINISHED!!"
        cleanup:
        println "CLEANUP STARTEE!!"

        WSSimulator.shutdown()

    }

}
