package com.example;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class DatabaseMigrationTest {

    private static JdbcTemplate jdbcTemplate;

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer;

    static {
        try (PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2")) {
            postgreSQLContainer
                    .withDatabaseName("bookshelf")
                    .withUsername("postgres")
                    .withPassword("postgres");
            DatabaseMigrationTest.postgreSQLContainer = postgreSQLContainer;
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

        jdbcTemplate = new JdbcTemplate(dataSource);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }

    @Test
    void testThatContainerIsRunning() {
        assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    void testRolesTable() {
        Integer rolesRowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM roles", Integer.class);
        assertEquals(3, rolesRowCount);
    }

    @Test
    void testRelationTypesTable() {
        Integer relationTypesRowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM relation_types", Integer.class);
        assertEquals(4, relationTypesRowCount);
    }

    @Test
    void testGenresTable() {
        Integer genresRowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM genres", Integer.class);
        assertEquals(24, genresRowCount);
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }
}
