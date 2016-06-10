package wssimulator

import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.core.IsEqual.equalTo

class HappyPathTestSpecification extends Specification {

    def "Simple simulator Test using file: simple.yaml"() {
        setup:
        int port = TestUtils.randomPort()
        when:
        WSSimulator.setPort(port)
        WSSimulator.addSimulation(new File(getClass().getResource("/simple.yml").toURI()))
        then:
        given().port(port).get("/hello").then().assertThat()
                .statusCode(201).and().body(equalTo("hello world"))
        cleanup:
        WSSimulator.shutdown() 
    }

    def "simple2.yaml test"() {
        setup:
        int port = TestUtils.randomPort()
        when:
        WSSimulator.setPort(port)
        WSSimulator.addSimulation(new File(getClass().getResource("/simple2.yml").toURI()))
        then:
        given().port(port).get("/hello/tester").then().assertThat()
                .statusCode(201).and().body(equalTo("Hello World tester"))
        cleanup:
        WSSimulator.shutdown()
    }

    def "test adding more than 1 simulation"() {
        setup:
        int port = TestUtils.randomPort()
        when:
        WSSimulator.setPort(port)
        WSSimulator.addSimulation(new File(getClass().getResource("/simple2.yml").toURI()))
        WSSimulator.addSimulation(new File(getClass().getResource("/simple3.yml").toURI()))
        then:
        WSSimulator.loadedSimulationCount() == 2
        given().port(port).get("/hello/tester").then().assertThat()
                .statusCode(201).and().body(equalTo("Hello World tester"))
        given().port(port).get("/test").then().assertThat()
                .statusCode(201)
        cleanup:
        WSSimulator.shutdown() 
    }


    def "test passing YAML in as a string"() {
        setup:
        int port = TestUtils.randomPort()
        when:
        WSSimulator.setPort(port)
        WSSimulator.addSimulation("path: /hello/:name\n" +
                "response: Hello World \${param.name}")

        then:
        WSSimulator.loadedSimulationCount() == 1
        given().port(port).get("/hello/tester").then().assertThat()
                .statusCode(201).and().body(equalTo("Hello World tester"))
        cleanup:
        WSSimulator.shutdown() 
    }


}
