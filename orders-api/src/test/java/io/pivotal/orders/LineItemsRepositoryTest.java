package io.pivotal.orders;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
    initializers = { LineItemsRepositoryTest.TestContainerInitializer.class })
public class LineItemsRepositoryTest {

    private static OracleContainer oracleContainer = 
        (OracleContainer) new OracleContainer().withStartupTimeoutSeconds(450);

    @Autowired
    private OrdersRepository ordersRepo;

    @Autowired
    private LineItemsRepository lineItemsRepo;

    @BeforeAll
    public static void startup() {
        oracleContainer.start();
    }

    @AfterAll
    public static void shutdown() {
        oracleContainer.stop();
    }

    @Test
    public void cannotCreateLineItemBecauseItWasNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> lineItemsRepo.create(null));
    }

    @Test
    public void cannotFindLineItemBecauseIdWasNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> lineItemsRepo.findById(null));
    }

    @Test
    public void cannotDeleteLineItemBecauseIdWasNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> lineItemsRepo.delete(null));
    }

    @Test
    public void canCreateFindAndDeleteALineItem() {
        Assertions.assertEquals(0, ordersRepo.count());
        Assertions.assertEquals(0, lineItemsRepo.count());
        UUID orderId = 
            ordersRepo
                .create(
                    Order.
                        newInstance()
                            .requestedBy("tony.stark@starkenterprises.com")
                            .orderedBy("nick.fury@shield.com")
                            .branch("AVE")
                            .supplier("Stark Enterprises")
                            .remarks("for Thanos")
                            .status("REQUESTED")
                );
        UUID lineItemId = 
            lineItemsRepo
                .create(
                    LineItem
                        .newInstance()
                            .orderId(orderId)
                            .itemCode("BERTHA")
                            .itemDescription("Near Earth orbit Hulk buster suit of armor")
                            .quantity(1L)
                            .unitPrice(15000000.00)
                            .unitOfMeasure("PC"));
        Assertions.assertEquals(1, lineItemsRepo.count());
        LineItem lineItem = lineItemsRepo.findById(lineItemId);
        Assertions.assertTrue(lineItem != null);
        lineItemsRepo.delete(lineItemId);
        ordersRepo.delete(orderId);
        Assertions.assertEquals(0, lineItemsRepo.count());
        Assertions.assertEquals(0, ordersRepo.count());
    }

    @Test
    @Disabled("Temporarily disabled. Liquibase is not creating stored procedure. Further research required.")
    public void canCreateLineItemThenFindByOrderId() {
        Assertions.assertEquals(0, ordersRepo.count());
        Assertions.assertEquals(0, lineItemsRepo.count());
        UUID orderId = 
            ordersRepo
                .create(
                    Order.
                        newInstance()
                            .requestedBy("tony.stark@starkenterprises.com")
                            .orderedBy("nick.fury@shield.com")
                            .branch("AVE")
                            .supplier("Stark Enterprises")
                            .remarks("for Thanos")
                            .status("REQUESTED")
                );
        UUID lineItemId = 
            lineItemsRepo
                .create(
                    LineItem
                        .newInstance()
                            .orderId(orderId)
                            .itemCode("BERTHA")
                            .itemDescription("Near Earth orbit Hulk buster suit of armor")
                            .quantity(1L)
                            .unitPrice(15000000.00)
                            .unitOfMeasure("PC"));
        List<LineItem> lineItems = lineItemsRepo.findByOrderId(orderId);
        Assertions.assertNotNull(lineItems);
        Assertions.assertEquals(1, lineItems.size());
        lineItemsRepo.delete(lineItemId);
        ordersRepo.delete(orderId);
        Assertions.assertEquals(0, lineItemsRepo.count());
        Assertions.assertEquals(0, ordersRepo.count());
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