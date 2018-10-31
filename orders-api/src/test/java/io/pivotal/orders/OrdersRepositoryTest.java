package io.pivotal.orders;

import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.OracleContainer;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(
    initializers = { OrdersRepositoryTest.TestContainerInitializer.class })
public class OrdersRepositoryTest {

    private static OracleContainer oracleContainer = 
        (OracleContainer) new OracleContainer().withStartupTimeoutSeconds(300);

    @Autowired
    private OrdersRepository repo;

    @BeforeAll
    public static void startup() {
        oracleContainer.start();
    }

    @AfterAll
    public static void shutdown() {
        oracleContainer.stop();
    }

    @Test
    public void canCreateFindAndDeleteAnOrder() {
        Assertions.assertEquals(0, repo.count());
        UUID newId = repo.create(Order.newInstance().requestedBy("tony.stark@starkenterprises.com").orderedBy("nick.fury@shield.com").branch("AVE").supplier("Stark Enterprises").remarks("for Thanos").status("REQUESTED"));
        Assertions.assertEquals(1, repo.count());
        Order order = repo.findById(newId);
        Assertions.assertTrue(order != null);
        repo.delete(newId);
        Assertions.assertEquals(0, repo.count());
    }

    static class TestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues
                .of(
                    "spring.datasource.url=" + oracleContainer.getJdbcUrl(),
                    "spring.datasource.username=" + oracleContainer.getUsername(),
                    "spring.datasource.password=" + oracleContainer.getPassword(),
                    "spring.datasource.driver-class-name=" + oracleContainer.getDriverClassName()
                )
                .applyTo(applicationContext.getEnvironment());
        }
    }
}