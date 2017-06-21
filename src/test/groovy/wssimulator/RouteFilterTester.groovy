package wssimulator

import io.restassured.http.ContentType
import org.slf4j.Logger
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.core.IsEqual.equalTo
import static org.slf4j.LoggerFactory.getLogger
import static wssimulator.scanner.SimulationFilters.byNamespace
import static wssimulator.scanner.SimulationFilters.byPackagePrefix
import static wssimulator.scanner.SimulationFilters.filters
import static wssimulator.scanner.SimulationScanner.classPathScanner

/**
 * Created by iankelly on 24/05/2017.
 *
 */
class RouteFilterTester extends Specification {
    private static final Logger LOG = getLogger(RouteFilterTester.class);

    def "Validate the route filter feature"() {
        setup:
        int port = TestUtils.randomPort()
        def simulator = WSSimulator.simulator(port)
        when:
        simulator.addSimulations(classPathScanner(filters(byPackagePrefix("route"),
                byNamespace("test.route.filter"))))
        then:
        LOG.info("Validate the route filter feature:then")
        given().port(port)
                .contentType(ContentType.XML)
                .body("This is just random test with Action1 contained within it")
                .post("/publish").then().assertThat()
                .statusCode(200).and().body(equalTo("FilteredByAction1"))
        LOG.info("Validate the route filter feature:then-1")

        given().port(port)
                .contentType(ContentType.XML)
                .body("This is just random test with Action2 contained within it")
                .post("/publish").then().assertThat()
                .statusCode(200).and().body(equalTo("FilteredByAction2"))
        LOG.info("Validate the route filter feature:then-2")

        given().port(port)
                .contentType(ContentType.XML)
                .body("This is just random test with Action3 contained within it")
                .post("/publish").then().assertThat()
                .statusCode(200).and().body(equalTo("FilteredByAction3"))

        given().port(port)
                .contentType(ContentType.XML)
                .body("This is just random test with no action mapped contained within it")
                .post("/publish").then().assertThat()
                .statusCode(200).and().body(equalTo("FilteredByAction4"))
        LOG.info("Validate the route filter feature:end")

        cleanup:
        LOG.info("Validate the route filter feature:cleanup")
        simulator.shutdown()
    }

}
