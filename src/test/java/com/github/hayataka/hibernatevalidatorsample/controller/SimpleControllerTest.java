package com.github.hayataka.hibernatevalidatorsample.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayataka.hibernatevalidatorsample.context.ThreadContext;
import com.github.hayataka.hibernatevalidatorsample.dto.SimpleDto;
import com.github.hayataka.hibernatevalidatorsample.interceptor.ClientLocaleMessageInterpolator;

public class SimpleControllerTest {

	private static final Logger log = LoggerFactory.getLogger(SimpleControllerTest.class);

	/**
	 * validatorとはあまり関係のない、jarの依存関係テスト. コンソールにlogが出力されていることを確認する物
	 */
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

	/**
	 * 基本的なvalidatorの使い方を確認する物. hibernate−validator標準にあるvalidationのみで確認する
	 */
	@Test
	public void firstStep() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		SimpleDto dto = new SimpleDto();
		dto.setMustBeTrue(false);
		Set<ConstraintViolation<SimpleDto>> violations = validator.validate(dto);
		assertThat("aaaはtrueじゃないといけないので一つエラーがある", violations.size(), is(1));
		for (ConstraintViolation<SimpleDto> violation : violations) {
			log.info(violation.getMessage()); // 実メッセージ
			log.info(violation.getMessageTemplate()); // 実メッセージを取得するためのキー
			log.info("{}", violation.getInvalidValue()); // エラーと判定された値
			log.info("{}", violation.getPropertyPath()); // DTO上のフィールド名
		}
	}

	/**
	 * decimal系だけの範囲チェック処理の確認.
	 */
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

	/**
	 * 他言語対応した場合の挙動確認.
	 */
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
				log.info("This must be English Message");
				log.info("getMessageで取得した値：{}", violation.getMessage());

				// // // {javax.validation.constraints.DecimalMax.message}
				// // というキーを表す値
				// log.info("getMessageTemplageで取得した値：{}",
				// violation.getMessageTemplate());
				//
				// // エラーが発生した時の値
				// log.info("エラーが発生した時の値＝getInvalidValue:  {}",
				// violation.getInvalidValue());
				//
				// // エラーが発生したDTOのフィールド名
				// log.info("property path is {}", violation.getPropertyPath());
			}

			context.set(Locale.JAPAN);
			violations = validator.validate(dto);
			assertThat("境界値超え", violations.size(), is(1));
			for (ConstraintViolation<SimpleDto> violation : violations) {
				log.info("ここは日本語メッセージであるべき");
				log.info("getMessageで取得した値：{}", violation.getMessage());
			}

		}

	}

	/**
	 * 一番基本的なvalidator返却.
	 * @return hibernate−validatorで基本的に定義されているvalidator
	 */
	private Validator getValidator() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		return validator;
	}

	/**
	 * 多言語対応用.
	 * @return 他言語対応したvalidator
	 */
	private Validator getMultiLanguageValidator() {

		Configuration<?> config = Validation.byDefaultProvider().configure();
		Configuration<?> mi = config.messageInterpolator(new ClientLocaleMessageInterpolator(new ResourceBundleMessageInterpolator()));
		Validator validator = mi.buildValidatorFactory().getValidator();
		return validator;
	}

}
