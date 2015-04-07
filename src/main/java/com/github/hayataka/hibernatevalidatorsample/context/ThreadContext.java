package com.github.hayataka.hibernatevalidatorsample.context;

import java.util.Locale;
/**
 * Thread内での破棄をする仕組みが大前提
 * @author hayakawatakahiko
 *
 */
public class ThreadContext implements AutoCloseable {

	private static ThreadContext instance;
	static {
		instance = new ThreadContext();
	}
	public static ThreadContext getInstance() {
		return instance;
	}

	private static ThreadLocal<Locale> data = new ThreadLocal<Locale>() {
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

	@Override
	public void close() {
		data.remove();	}
}
