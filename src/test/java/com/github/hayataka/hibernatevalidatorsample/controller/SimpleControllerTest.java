package com.github.hayataka.hibernatevalidatorsample.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.AggregateResourceBundleLocator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayataka.hibernatevalidatorsample.context.ThreadContext;
import com.github.hayataka.hibernatevalidatorsample.dto.SimpleDto;
import com.github.hayataka.hibernatevalidatorsample.interceptor.ClientLocaleMessageInterpolator;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class SimpleControllerTest {

	private static final Logger log = LoggerFactory.getLogger(SimpleControllerTest.class);

	@Test
	public void logOutPUtCheckOnly() {
		log.info("slf4j-api out put ");
		// slf4j-simple, default info level;
		if (log.isDebugEnabled()) {
			log.error("debug level outputs");
		} else {
			log.info("not debug level");
		}
		assertThat(1, is(1));
	}

	@Test
	public void firstStep() throws Exception {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		SimpleDto dto = new SimpleDto();
		dto.setMustBeTrue(false);
		Set<ConstraintViolation<SimpleDto>> violations = validator.validate(dto);
		assertThat("aaaはtrueじゃないといけないので一つエラーがある", violations.size(), is(1));
		for (ConstraintViolation<SimpleDto> violation : violations) {
			log.info(violation.getMessage());
		}
	}

	@Test
	public void decimalPatternTest() {

		Validator validator = this.getValidator();

		{
			SimpleDto dto = new SimpleDto();
			dto.setBigDecimalValue(new BigDecimal("4.99"));
			dto.setMustBeTrue(true);

			Set<ConstraintViolation<SimpleDto>> violations = validator.validate(dto);
			assertThat("範囲内正常", violations.size(), is(0));
		}

		{
			SimpleDto dto = new SimpleDto();
			dto.setBigDecimalValue(new BigDecimal("5.00"));
			dto.setMustBeTrue(true);

			Set<ConstraintViolation<SimpleDto>> violations = validator.validate(dto);
			assertThat("境界値", violations.size(), is(0));
		}

		{
			SimpleDto dto = new SimpleDto();
			dto.setStringValue("101");
			dto.setIntValue(50);
			dto.setMustBeTrue(true);

			Set<ConstraintViolation<SimpleDto>> violations = validator.validate(dto);
			assertThat("String/intなどにも設定可能な事の確認", violations.size(), is(0));
		}

	}

	/**
	 * エラーが発生した時にどのような情報を取得することができるかを明らかにする。
	 */
	@Test
	public void errorInformationExplain() {

		SimpleDto dto = new SimpleDto();
		dto.setBigDecimalValue(new BigDecimal("5.01"));
		dto.setMustBeTrue(true);

		Set<ConstraintViolation<SimpleDto>> violations = getValidator().validate(dto);
		assertThat("境界値超え", violations.size(), is(1));
		for (ConstraintViolation<SimpleDto> violation : violations) {
			log.info("getMessageで取得した値：{}", violation.getMessage());

			// // {javax.validation.constraints.DecimalMax.message} というキーを表す値
			log.info("getMessageTemplageで取得した値：{}", violation.getMessageTemplate());

			// エラーが発生した時の値
			log.info("エラーが発生した時の値＝getInvalidValue:  {}", violation.getInvalidValue());

			// エラーが発生したDTOのフィールド名
			log.info("property path is {}", violation.getPropertyPath());
		}
	}

	@Test
	public void multiLanguageTest() {

		
		
		
		try (ThreadContext context = ThreadContext.getInstance()) {

			context.set(Locale.ENGLISH);

			SimpleDto dto = new SimpleDto();
			dto.setBigDecimalValue(new BigDecimal("5.01"));
			dto.setMustBeTrue(true);

			Validator validator = getMultiLanguageValidator();
			Set<ConstraintViolation<SimpleDto>> violations = validator.validate(dto);
			assertThat("境界値超え", violations.size(), is(1));
			for (ConstraintViolation<SimpleDto> violation : violations) {
				log.info("getMessageで取得した値：{}", violation.getMessage());

//				// // {javax.validation.constraints.DecimalMax.message}
//				// というキーを表す値
//				log.info("getMessageTemplageで取得した値：{}", violation.getMessageTemplate());
//
//				// エラーが発生した時の値
//				log.info("エラーが発生した時の値＝getInvalidValue:  {}", violation.getInvalidValue());
//
//				// エラーが発生したDTOのフィールド名
//				log.info("property path is {}", violation.getPropertyPath());
			}


			context.set(Locale.JAPAN);
			violations =			validator.validate(dto);
			assertThat("境界値超え", violations.size(), is(1));
			for (ConstraintViolation<SimpleDto> violation : violations) {
				log.info("getMessageで取得した値：{}", violation.getMessage());

//				// // {javax.validation.constraints.DecimalMax.message}
//				// というキーを表す値
//				log.info("getMessageTemplageで取得した値：{}", violation.getMessageTemplate());
//
//				// エラーが発生した時の値
//				log.info("エラーが発生した時の値＝getInvalidValue:  {}", violation.getInvalidValue());
//
//				// エラーが発生したDTOのフィールド名
//				log.info("property path is {}", violation.getPropertyPath());
			}
			
		}

	}

	/**
	 * 一番基本的なvalidator返却.
	 * 
	 * @return
	 */
	private Validator getValidator() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		return validator;
	}

	/**
	 * 多言語対応用.
	 * 
	 * @return
	 */
	private Validator getMultiLanguageValidator() {
//
//		Validator validator2 = Validation.byDefaultProvider().configure()
//						.messageInterpolator(new CustomMessageInterpolator(Locale.JAPANESE))
//				.buildValidatorFactory().getValidator();

		Configuration<?> config = Validation.byDefaultProvider().configure();
//		config = config.messageInterpolator(new ClientLocaleMessageInterpolator());

//		Validator validator = config.buildValidatorFactory().getValidator();
		Validator validator = 		config.messageInterpolator(
				new ClientLocaleMessageInterpolator(
//						new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator(ResourceBundleMessageInterpolator.USER_VALIDATION_MESSAGES),true))).buildValidatorFactory().getValidator();
						new ResourceBundleMessageInterpolator())).buildValidatorFactory().getValidator();
// config		 .messageInterpolator(
//	                new ResourceBundleMessageInterpolator(
//	                        new AggregateResourceBundleLocator(
//	                                Arrays.asList(
//	                                        "MyMessages",
//	                                        "MyOtherMessages"
//	                                )
//	                        )
//	                )
//	        );
		
		

		return validator;
	}

}
