package com.github.hayataka.hibernatevalidatorsample.context;

import java.io.Closeable;
/**
 * tryでのresourceCloseを行うための仕組
 * @author hayakawatakahiko
 */
interface AutoCloseable extends Closeable {
	/**
	 * 開放すべきリソースを閉じる処理.
	 */
	void close();
}
