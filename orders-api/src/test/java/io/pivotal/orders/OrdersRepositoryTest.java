package io.pivotal.orders;

import java.time.LocalDate;
import java.util.List;
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
        (OracleContainer) new OracleContainer().withStartupTimeoutSeconds(450);

    @Autowired
    private OrdersRepository orderRepo;

    @BeforeAll
    public static void startup() {
        oracleContainer.start();
    }

    @AfterAll
    public static void shutdown() {
        oracleContainer.stop();
    }

    @Test
    public void cannotCreateOrderBecauseItWasNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderRepo.create(null));
    }

    @Test
    public void cannotCreateOrderBecauseIdWasNotNull() {
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> orderRepo.create(PurchaseOrderTestUtil.vendInvalidDetachedOrder()));
    }

    @Test
    public void cannotFindOrderBecauseIdWasNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderRepo.findById(null));
    }

    @Test
    public void cannotDeleteOrderBecauseIdWasNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderRepo.delete(null));
    }

    @Test
    public void canCreateFindAndDeleteAnOrder() {
        Assertions.assertEquals(0, orderRepo.count());
        UUID newId = 
            orderRepo
                .create(
                    Order
                        .newInstance()
                            .requestedBy("tony.stark@starkenterprises.com")
                            .orderedBy("nick.fury@shield.com")
                            .branch("AVE")
                            .supplier("Stark Enterprises")
                            .remarks("for Thanos")
                            .status("REQUESTED")
                );
        Assertions.assertEquals(1, orderRepo.count());
        Order order = orderRepo.findById(newId);
        Assertions.assertTrue(order != null);
        orderRepo.delete(newId);
        Assertions.assertEquals(0, orderRepo.count());
    }

    @Test
    public void canCreateOrderThenFindByDateCreated() {
        Assertions.assertEquals(0, orderRepo.count());
        UUID newId = 
            orderRepo
                .create(
                    Order
                        .newInstance()
                            .requestedBy("tony.stark@starkenterprises.com")
                            .orderedBy("nick.fury@shield.com")
                            .branch("AVE")
                            .supplier("Stark Enterprises")
                            .remarks("for Thanos")
                            .status("REQUESTED")
                );
        List<Order> orders = orderRepo.findByCreatedDate(LocalDate.now().plusDays(1));
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(1, orders.size());
        orderRepo.delete(newId);
        Assertions.assertEquals(0, orderRepo.count());
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