package com.github.hayataka.hibernatevalidatorsample.interceptor;

import java.util.Locale;

import javax.validation.MessageInterpolator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;

import com.github.hayataka.hibernatevalidatorsample.context.LocaleUtil;
import com.github.hayataka.hibernatevalidatorsample.context.ThreadContext;

public class ClientLocaleMessageInterpolator implements MessageInterpolator {
	
	private final MessageInterpolator mi;

	public ClientLocaleMessageInterpolator(MessageInterpolator param) {
//		this.mi = new ResourceBundleMessageInterpolator();
		this.mi = param;
	}
	
	//	public ClientLocaleMessageInterpolator(MessageInterpolator delegate) {
//		this.delegate = delegate;
//	}

	@Override
	public String interpolate(String message, Context context) {
		
		final Locale locale = LocaleUtil.get();
		return interpolate(message, context, locale);
	}

	// memo ここに指定したぴったりの名前のLocale指定された ValidationMessage_xx.properties
	// が存在しないときは、実行環境のlocaleを初期値として取り扱うような挙動をしています。
	
	@Override
	public String interpolate(String message, Context context, Locale locale) {
		return mi.interpolate(message, context, locale);
	}
}