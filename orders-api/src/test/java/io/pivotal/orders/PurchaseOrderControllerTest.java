package io.pivotal.orders;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@SpringJUnitWebConfig(OrdersApiApplication.class)
@WebMvcTest(controllers = { PurchaseOrderController.class }, secure = false)
public class PurchaseOrderControllerTest {

    private static final LocalDateTime DATE_CREATED = LocalDateTime.now();
    private static final UUID ORDER_ID = UUID.randomUUID();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PurchaseOrdersService service;

    @Test
    public void givenPurchaseOrders_whenGetPurchaseOrderByOrderId_thenStatus200() throws Exception {
        UUID lineItemId = UUID.randomUUID();
        when(service.findPurchaseOrderByOrderId(ORDER_ID)).thenReturn(vendPurchaseOrder(ORDER_ID, lineItemId));

        mvc
            .perform(get(String.format("/purchaseOrders/%s", ORDER_ID.toString()))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.order.id", is(ORDER_ID.toString())))
            .andExpect(jsonPath("$.lineItems[0].id", is(lineItemId.toString())));
    }

    @Test
    public void givenNoPurchaseOrders_whenGetPurchaseOrderByOrderId_thenStatus404() throws Exception {
        when(service.findPurchaseOrderByOrderId(ORDER_ID)).thenThrow(EmptyResultDataAccessException.class);
        mvc
            .perform(get(String.format("/purchaseOrders/%s", ORDER_ID.toString()))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void givenPurchaseOrders_whenGetPurchaseOrderByDateCreated_thenStatus200() throws Exception {
        when(service.findPurchaseOrderByOrderDateCreated(DATE_CREATED.toLocalDate())).thenReturn(vendPurchaseOrders(DATE_CREATED));

        mvc
            .perform(get("/purchaseOrders")
            .param("dateCreated", DATE_CREATED.toLocalDate().toString())
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].order.id", is(ORDER_ID.toString())))
            .andExpect(jsonPath("[0].order.dateCreated", greaterThan(DATE_CREATED.toLocalDate().toString())));
    }

    @Test
    public void givenNoPurchaseOrders_whenGetPurchaseOrderByDateCreated_thenStatus404() throws Exception {
        when(service.findPurchaseOrderByOrderDateCreated(DATE_CREATED.toLocalDate())).thenThrow(EmptyResultDataAccessException.class);

        mvc
            .perform(get("/purchaseOrders")
            .param("dateCreated", DATE_CREATED.toLocalDate().toString())
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound());  
    }

    @Test
    public void givenPurchaseOrder_whenPostPurchaseOrder_thenStatus201() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID lineItemId = UUID.randomUUID();
        PurchaseOrder detachedPO = vendPurchaseOrder();
        doReturn(vendPurchaseOrder(orderId, lineItemId)).when(service).createPurchaseOrder(detachedPO);

        mvc
            .perform(post("/purchaseOrders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(detachedPO))
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    public void givenOrderId_whenDeletePurchaseOrder_thenStatus202() throws Exception {
        doNothing().when(service).deletePurchaseOrderByOrderId(ORDER_ID);

        mvc
            .perform(delete(String.format("/purchaseOrders/%s", ORDER_ID.toString()))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isAccepted());
    }

    private List<PurchaseOrder> vendPurchaseOrders(LocalDateTime dateCreated) {
        Order order = 
            new Order(ORDER_ID, "tony.stark@starkenterprises.com", "nick.fury@shield.com",
                "AVE", "Stark Enterprises", "for Thanos", DATE_CREATED, "REQUESTED");
        LineItem lineItem = 
            LineItem
                .newInstance()
                    .id(UUID.randomUUID())
                    .orderId(ORDER_ID)
                    .itemCode("BERTHA")
                    .itemDescription("Near Earth orbit Hulk buster suit of armor")
                    .quantity(1L)
                    .unitPrice(15000000.00)
                    .unitOfMeasure("PC");
        return Arrays.asList(new PurchaseOrder(order, Arrays.asList(lineItem)));
    }

    private PurchaseOrder vendPurchaseOrder(UUID orderId, UUID lineItemId) {
        Order order = 
            Order
                .newInstance()
                    .id(orderId)
                    .requestedBy("tony.stark@starkenterprises.com")
                    .orderedBy("nick.fury@shield.com")
                    .branch("AVE")
                    .supplier("Stark Enterprises")
                    .remarks("for Thanos")
                    .status("REQUESTED");
        LineItem lineItem = 
            LineItem
                .newInstance()
                    .id(lineItemId)
                    .orderId(orderId)
                    .itemCode("BERTHA")
                    .itemDescription("Near Earth orbit Hulk buster suit of armor")
                    .quantity(1L)
                    .unitPrice(15000000.00)
                    .unitOfMeasure("PC");
        return new PurchaseOrder(order, Arrays.asList(lineItem));
    }

    private PurchaseOrder vendPurchaseOrder() {
        Order order = 
            Order
                .newInstance()
                    .requestedBy("tony.stark@starkenterprises.com")
                    .orderedBy("nick.fury@shield.com")
                    .branch("AVE")
                    .supplier("Stark Enterprises")
                    .remarks("for Thanos")
                    .status("REQUESTED");
        LineItem lineItem = 
            LineItem
                .newInstance()
                    .itemCode("BERTHA")
                    .itemDescription("Near Earth orbit Hulk buster suit of armor")
                    .quantity(1L)
                    .unitPrice(15000000.00)
                    .unitOfMeasure("PC");
        return new PurchaseOrder(order, Arrays.asList(lineItem));
    }

    private String asJsonString(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException(jpe);
        }
    }  

    @ControllerAdvice
    static  class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
 
        @ExceptionHandler({ EmptyResultDataAccessException.class })
        public ResponseEntity<Object> handleEmptyResultDataAccessException(Exception ex, WebRequest request) {
            return new ResponseEntity<Object>(
                "No results found matching criteria", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
     
    }

}