package wssimulator

import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.core.IsEqual.equalTo

class HappyPathTestSpecification extends Specification {

    def "Simple simulator Test using file: simple.yaml"() {
        given:
        WSSimulator.addSimulation(new File(getClass().getResource("/simple.yml").toURI()))
        expect:
        given().port(4567).get("/hello").then().assertThat()
                .statusCode(201).and().body(equalTo("hello world"))
        cleanup:
        WSSimulator.shutdown();
    }

    def "simple2.yaml test"() {
        given:
        WSSimulator.addSimulation(new File(getClass().getResource("/simple2.yml").toURI()))
        expect:
        given().port(4567).get("/hello/tester").then().assertThat()
                .statusCode(201).and().body(equalTo("Hello World tester"))
        cleanup:
        WSSimulator.shutdown();
    }

    def "test adding more than 1 simulation"() {
        given:
        WSSimulator.addSimulation(new File(getClass().getResource("/simple2.yml").toURI()))
        WSSimulator.addSimulation(new File(getClass().getResource("/simple3.yml").toURI()))
        expect:
        WSSimulator.loadedSimulationCount()==2
        given().port(4567).get("/hello/tester").then().assertThat()
                .statusCode(201).and().body(equalTo("Hello World tester"))
        given().port(4567).get("/test").then().assertThat()
                .statusCode(201)
        cleanup:
        WSSimulator.shutdown();
    }


    def "test passing YAML in as a string"() {
        given:
        WSSimulator.addSimulation("path: /hello/:name\n" +
                "response: Hello World \${param.name}")

        expect:
        WSSimulator.loadedSimulationCount()==1
        given().port(4567).get("/hello/tester").then().assertThat()
                .statusCode(201).and().body(equalTo("Hello World tester"))
        cleanup:
        WSSimulator.shutdown();
    }


}
