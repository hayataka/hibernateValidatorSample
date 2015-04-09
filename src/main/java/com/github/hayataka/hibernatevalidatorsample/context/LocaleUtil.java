package com.github.hayataka.hibernatevalidatorsample.context;

import java.util.Locale;

public class LocaleUtil {

	/**
	 * Locale取得をThreadContextから行う.
	 * @return 利用ユーザのLocale
	 */
	public static Locale get() {
		return ThreadContext.getInstance().get();
	}
}
