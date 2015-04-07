package com.github.hayataka.hibernatevalidatorsample.context;

import java.util.Locale;

public class LocaleUtil {

	public static Locale get() {
		return ThreadContext.getInstance().get();
	}
}
