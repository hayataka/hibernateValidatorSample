package com.github.hayataka.hibernatevalidatorsample.interceptor;

import java.util.Locale;

import javax.validation.MessageInterpolator;

import com.github.hayataka.hibernatevalidatorsample.context.LocaleUtil;
/**
 * ユーザのLocaleに合わせたメッセージを返却するためのinterpolator.
 * @author hayakawatakahiko
 */
public class ClientLocaleMessageInterpolator implements MessageInterpolator {

	/**
	 * デザインパターンでいうデコレーションパターン用.
	 */
	private final MessageInterpolator mi;

	/**
	 * コンストラクタ.
	 * @param param 次に渡すinterpolator
	 */
	public ClientLocaleMessageInterpolator(MessageInterpolator param) {
		this.mi = param;
	}
	
	/**
	 * 文字列置換処理前にlocaleをユーザに合わせて変更する.
	 * @param message 実メッセージを返却するための、メッセージキー
	 * @param context messageInterpolatorContext
	 */
	@Override
	public String interpolate(String message, Context context) {
		final Locale locale = LocaleUtil.get();
		return interpolate(message, context, locale);
	}



	/**
	 * メッセージ置換処理を実施する.
	 * memo ここに指定したぴったりの名前のLocale指定された ValidationMessage_xx.properties
	 * が存在しないときは、アプリ側OS 実行環境のlocaleを初期値として取り扱うような挙動をしています。
	 * @param message 実メッセージを取得するためのキー
	 * @param context messageInterpolatorContext
	 * @param locale ユーザlocale
	 */
	@Override
	public String interpolate(String message, Context context, Locale locale) {
		return mi.interpolate(message, context, locale);
	}
}