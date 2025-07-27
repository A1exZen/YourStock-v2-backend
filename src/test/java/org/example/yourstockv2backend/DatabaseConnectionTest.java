package org.example.yourstockv2backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class DatabaseConnectionTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnection() throws Exception {
        // Arrange
        Connection connection = dataSource.getConnection();

        // Act
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE test_table (id SERIAL PRIMARY KEY, name VARCHAR(255))");
        statement.execute("INSERT INTO test_table (name) VALUES ('Test')");
        ResultSet resultSet = statement.executeQuery("SELECT name FROM test_table WHERE id = 1");

        // Assert
        assertNotNull(connection);
        assertTrue(resultSet.next());
        assertEquals("Test", resultSet.getString("name"));

        // Cleanup
        statement.execute("DROP TABLE test_table");
        connection.close();
    }
}