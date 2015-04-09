package com.github.hayataka.hibernatevalidatorsample.context;

import java.util.Locale;

/**
 * Thread内での情報をすべて格納しているクラス. AutoCloseableの意図はtry()を用いるため
 * 
 * @author hayakawatakahiko
 */
public class ThreadContext implements AutoCloseable {

	private static ThreadContext instance;
	static {
		instance = new ThreadContext();
	}

	public static ThreadContext getInstance() {
		return instance;
	}

	/**
	 * 初期値＝日本.
	 */
	private static ThreadLocal<Locale> data = new ThreadLocal<Locale>() {
		/**
		 * 初期値＝日本.
		 * 
		 * @return 日本
		 */
		@Override
		protected Locale initialValue() {
			return Locale.JAPAN;
		}
	};

	public Locale get() {
		return data.get();
	}

	public void set(Locale locale) {
		data.set(locale);
	}

	/**
	 * try()で閉じる.
	 */
	@Override
	public void close() {
		data.remove();
	}
}
