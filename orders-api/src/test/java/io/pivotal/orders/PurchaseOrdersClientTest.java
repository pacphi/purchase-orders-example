package io.pivotal.orders;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.OracleContainer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(
    initializers = { PurchaseOrdersClientTest.TestContainerInitializer.class })
public class PurchaseOrdersClientTest {

    private static OracleContainer oracleContainer = 
        (OracleContainer) new OracleContainer().withStartupTimeoutSeconds(450);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public static void startup() {
        oracleContainer.start();
    }

    @AfterAll
    public static void shutdown() {
        oracleContainer.stop();
    }

    private String createUrlWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void canCreatePurchaseOrder() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response = 
            restTemplate.postForEntity(
                createUrlWithPort("/purchaseOrders"), 
                new HttpEntity<PurchaseOrder>(
                    PurchaseOrderTestUtil.vendDetachedPurchaseOrder(),
                    headers), 
                String.class);
        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));
        Assertions.assertTrue(response.getHeaders().containsKey("Location"));
    }

    static class TestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues
                .of(
                    "spring.datasource.url=" + oracleContainer.getJdbcUrl(),
                    "spring.datasource.username=" + oracleContainer.getUsername(),
                    "spring.datasource.password=" + oracleContainer.getPassword(),
                    "spring.datasource.driver-class-name=" + oracleContainer.getDriverClassName(),
                    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration"
                )
                .applyTo(applicationContext.getEnvironment());
        }
    }
}