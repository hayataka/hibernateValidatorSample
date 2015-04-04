package com.github.hayataka.hibernatevalidatorsample.controller;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayataka.hibernatevalidatorsample.dto.SimpleDto;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class SimpleControllerTest {

	private static final Logger log = LoggerFactory.getLogger(SimpleControllerTest.class);

	@Test
	public void test() {
		
		log.info("aaaaaaaaa");
		
		assertThat(1, is(1));
	}
	
	 @Test
	    public void 最初の一歩() throws Exception {
	        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	        Validator validator = validatorFactory.getValidator();
	        SimpleDto dto = new SimpleDto();
	        dto.setAaa(false);
	        Set<ConstraintViolation<SimpleDto>> violations = validator.validate(dto);
	        assertThat("aaaはtrueじゃないといけないので一つエラーがある", violations.size(), is(1));
	        for (ConstraintViolation<SimpleDto> violation : violations) {
	            System.out.println(violation.getMessage());
	        }
	    }

}
