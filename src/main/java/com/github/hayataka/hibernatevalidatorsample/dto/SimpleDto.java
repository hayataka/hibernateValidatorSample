package com.github.hayataka.hibernatevalidatorsample.dto;

import java.math.BigDecimal;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

/**
 * 単純テスト用.
 * @author hayakawatakahiko
 */
public class SimpleDto {
	@AssertTrue
	private boolean mustBeTrue;

	@DecimalMin("100")
	@DecimalMax("1000")
	private String stringValue;

	@DecimalMax("50")
	private int intValue;

	//ここを定数化してしまえば、項目要素も他言語対応しやすいのでは？
	@DecimalMax(value="5.00",message="{decimalMax}{javax.validation.constraints.DecimalMax.message}")
	private BigDecimal bigDecimalValue;

	public boolean isMustBeTrue() {
		return mustBeTrue;
	}

	public void setMustBeTrue(boolean aaa) {
		this.mustBeTrue = aaa;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public BigDecimal getBigDecimalValue() {
		return bigDecimalValue;
	}

	public void setBigDecimalValue(BigDecimal bigDecimalValue) {
		this.bigDecimalValue = bigDecimalValue;
	}
}
