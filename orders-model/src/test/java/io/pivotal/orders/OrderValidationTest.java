package io.pivotal.orders;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class OrderValidationTest {

	private static ValidatorFactory validatorFactory;
    private static Validator validator;
 
    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
	}
	
	@Test
	public void isValidOrder() {
		Order order = Order
						.newInstance()
							.requestedBy("tony.stark@starkenterprises.com")
							.orderedBy("nick.fury@shield.com")
							.branch("AVE")
							.supplier("Stark Enterprises")
							.remarks("for Thanos")
							.status("REQUESTED");
		Set<ConstraintViolation<Order>> violations = validator.validate(order);
    	Assertions.assertTrue(violations.isEmpty());
	}

	@Test
	public void isNotValidOrderBecauseRequestedByIsNotValidEmailAddress() {
		Order order = Order
						.newInstance()
							.requestedBy("tony.stark")
							.orderedBy("nick.fury@shield.com")
							.branch("AVE")
							.supplier("Stark Enterprises")
							.remarks("for Thanos")
							.status("REQUESTED");
		Set<ConstraintViolation<Order>> violations = validator.validate(order);
		Assertions.assertEquals(1, violations.size());
		Assertions.assertEquals("requestedBy", violations.iterator().next().getPropertyPath().toString());
        Assertions.assertEquals("{javax.validation.constraints.Email.message}", violations.iterator().next().getMessageTemplate());
	}

	@Test
	public void isNotValidOrderBecauseOfMultipleConstraintViolations() {
		Order order = Order
						.newInstance()
							.requestedBy("tony.stark@starkenterprises.com")
							.orderedBy("nick.fury@shield.com")
							.supplier("Stark Enterprises")
							.remarks("for Thanos");
		Set<ConstraintViolation<Order>> violations = validator.validate(order);
		Assertions.assertEquals(2, violations.size());
	}

}
