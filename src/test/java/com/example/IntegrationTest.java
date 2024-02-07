package com.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8081"})
public class IntegrationTest {

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer;

    private static String accessToken;

    private static String refreshToken;

    static {
        RestAssured.port = 8081;

        try (PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2")) {
            postgreSQLContainer
                    .withDatabaseName("bookshelf")
                    .withUsername("postgres")
                    .withPassword("postgres");
            IntegrationTest.postgreSQLContainer = postgreSQLContainer;
        }
    }

    @DynamicPropertySource
    static void postgreSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
    }

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();

        DataSource dataSource = new DriverManagerDataSource(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }

    @Test
    @Order(1)
    public void testRegisterUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"test\", "
                        + "\"email\":\"test@gmail.com\", "
                        + "\"password\":\"password\", "
                        + "\"role\":[\"user\"]}")
                .when()
                .post("/auth/signup")
                .then()
                .statusCode(200)
                .body(equalTo("User registered successfully"));
    }

    @Test
    @Order(2)
    public void testAuthenticateUser() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"emailOrUsername\":\"test\", \"password\":\"password\"}")
                .when()
                .post("/auth/signin")
                .then()
                .statusCode(200)
                .extract().response();

        accessToken = response.path("accessToken");
        refreshToken = response.path("refreshToken");
    }

    @Test
    @Order(3)
    public void testRefreshToken() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"refreshToken\":\"" + refreshToken + "\"}")
                .when()
                .post("/auth/refresh")
                .then()
                .statusCode(200)
                .extract().response();

        String oldRefreshToken = refreshToken;
        refreshToken = response.path("refreshToken");
        accessToken = response.path("accessToken");
        assertEquals(oldRefreshToken, refreshToken);
    }

    @Test
    @Order(4)
    public void testLogoutUser() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .post("/auth/signout")
                .then()
                .statusCode(200)
                .body(equalTo("User logged out successfully"));

        refreshToken = null;
    }
}
