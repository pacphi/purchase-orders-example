package io.pivotal.orders;

import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LineItemValidationTest {

	private static ValidatorFactory validatorFactory;
    private static Validator validator;
 
    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
	}
	
	@Test
	public void isValidLineItem() {
        UUID orderId = UUID.randomUUID();
        LineItem lineItem = LineItem
                                .newInstance()
                                    .orderId(orderId)
                                    .itemCode("BERTHA")
                                    .itemDescription("Near Earth orbit Hulk buster suit of armor")
                                    .quantity(1L)
                                    .unitPrice(15000000.00)
                                    .unitOfMeasure("PC");
		Set<ConstraintViolation<LineItem>> violations = validator.validate(lineItem);
    	Assertions.assertTrue(violations.isEmpty());
    }
    
    @Test
    public void isNotValidLineItemBecauseItemCodeIsEmpty() {
        UUID orderId = UUID.randomUUID();
        LineItem lineItem = LineItem
                                .newInstance()
                                    .orderId(orderId)
                                    .itemDescription("Near Earth orbit Hulk buster suit of armor")
                                    .quantity(1L)
                                    .unitPrice(15000000.00)
                                    .unitOfMeasure("PC");
        Set<ConstraintViolation<LineItem>> violations = validator.validate(lineItem);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("itemCode", violations.iterator().next().getPropertyPath().toString());
        Assertions.assertEquals("{javax.validation.constraints.NotEmpty.message}", violations.iterator().next().getMessageTemplate());
    }

    @Test
    public void isNotValidLineItemBecauseQuantityIsNull() {
        UUID orderId = UUID.randomUUID();
        LineItem lineItem = LineItem
                                .newInstance()
                                    .orderId(orderId)
                                    .itemCode("BERTHA")
                                    .itemDescription("Near Earth orbit Hulk buster suit of armor")
                                    .unitPrice(15000000.00)
                                    .unitOfMeasure("PC");
        Set<ConstraintViolation<LineItem>> violations = validator.validate(lineItem);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("quantity", violations.iterator().next().getPropertyPath().toString());
        Assertions.assertEquals("{javax.validation.constraints.NotNull.message}", violations.iterator().next().getMessageTemplate());
    }

    @Test
    public void isNotValidLineItemBecauseOfMultipleConstraintViolations() {
        UUID orderId = UUID.randomUUID();
        LineItem lineItem = LineItem
                                .newInstance()
                                    .orderId(orderId)
                                    .itemCode("BERTHA")
                                    .itemDescription("Near Earth orbit Hulk buster suit of armor");
        Set<ConstraintViolation<LineItem>> violations = validator.validate(lineItem);
        Assertions.assertEquals(3, violations.size());
    }

}