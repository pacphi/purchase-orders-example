package io.pivotal.orders;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
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
    private RestTemplate restTemplate;

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

    @TestConfiguration
    // @see https://stackoverflow.com/questions/32392634/spring-resttemplate-redirect-302/32393049
    static class RestTemplateConfig {

        @Bean
        public RestTemplate restTemplate(SecurityProperties securityProperties) {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            final HttpClient httpClient = 
                HttpClientBuilder
                    .create()
                        .setRedirectStrategy(new LaxRedirectStrategy())
                        .build();
            factory.setHttpClient(httpClient);
            restTemplate.setRequestFactory(factory);
            return restTemplate;
        }
    }

    @TestConfiguration
    // @see https://stackoverflow.com/questions/49258766/spring-boot-2-0-x-disable-security-for-certain-profile
    static class ApplicationSecurityOverride extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(WebSecurity web) throws Exception {
            web
            .ignoring()
                .antMatchers("/**");
        }
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