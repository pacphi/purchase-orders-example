package io.pivotal.orders;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PurchaseOrderValidationTest {

	private static ValidatorFactory validatorFactory;
    private static Validator validator;
 
    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
	}
	
	@Test
	public void isValidPurchaseOrder() {
        UUID orderId = UUID.randomUUID();
		Order order = Order
                        .newInstance()
                            .id(orderId)
							.requestedBy("tony.stark@starkenterprises.com")
							.orderedBy("nick.fury@shield.com")
							.branch("AVE")
							.supplier("Stark Enterprises")
							.remarks("for Thanos")
                            .status("REQUESTED");
        LineItem lineItem = LineItem
                        .newInstance()
                            .orderId(orderId)
                            .itemCode("BERTHA")
                            .itemDescription("Near Earth orbit Hulk buster suit of armor")
                            .quantity(1L)
                            .unitPrice(15000000.00)
                            .unitOfMeasure("PC");
        List<LineItem> lineItems = new ArrayList<>();
        lineItems.add(lineItem);
        // with at least one line item
        PurchaseOrder purchaseOrder = new PurchaseOrder(order, lineItems);
		Set<ConstraintViolation<PurchaseOrder>> violations = validator.validate(purchaseOrder);
        Assertions.assertTrue(violations.isEmpty());
        // with no line items
        purchaseOrder = new PurchaseOrder(order, null);
		violations = validator.validate(purchaseOrder);
        Assertions.assertTrue(violations.isEmpty());
    }
    
    @Test
    public void isNotValidPurchaseOrderBecauseOrderIsNull() {
        LineItem lineItem = LineItem
                        .newInstance()
                            .itemCode("BERTHA")
                            .itemDescription("Near Earth orbit Hulk buster suit of armor")
                            .quantity(1L)
                            .unitPrice(15000000.00)
                            .unitOfMeasure("PC");
        List<LineItem> lineItems = new ArrayList<>();
        lineItems.add(lineItem);
        PurchaseOrder purchaseOrder = new PurchaseOrder(null, lineItems);
		Set<ConstraintViolation<PurchaseOrder>> violations = validator.validate(purchaseOrder);
        Assertions.assertEquals("order", violations.iterator().next().getPropertyPath().toString());
        Assertions.assertEquals("{javax.validation.constraints.NotNull.message}", violations.iterator().next().getMessageTemplate());
    }
    
}
