package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * Test de stress pour etudiant-service.
 * Lance avec : mvn gatling:test
 * Rapport HTML disponible dans target/gatling/
 */
public class EtudiantSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8081")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    ScenarioBuilder listeEtudiants = scenario("Liste des etudiants")
            .exec(http("GET /api/etudiants")
                    .get("/api/etudiants")
                    .check(status().is(200)));

    ScenarioBuilder getEtudiantById = scenario("Get etudiant par ID")
            .exec(http("GET /api/etudiants/1")
                    .get("/api/etudiants/1")
                    .check(status().in(200, 404)));

    ScenarioBuilder mixedLoad = scenario("Charge mixte")
            .exec(http("Liste")
                    .get("/api/etudiants")
                    .check(status().is(200)))
            .pause(1)
            .exec(http("Actuator Health")
                    .get("/actuator/health")
                    .check(status().is(200)));

    {
        setUp(
                listeEtudiants.injectOpen(
                        rampUsers(50).during(Duration.ofSeconds(30))
                ),
                mixedLoad.injectOpen(
                        constantUsersPerSec(5).during(Duration.ofSeconds(30))
                )
        ).protocols(httpProtocol)
         .assertions(
                 global().responseTime().percentile3().lt(2000),
                 global().successfulRequests().percent().gt(95.0)
         );
    }
}
