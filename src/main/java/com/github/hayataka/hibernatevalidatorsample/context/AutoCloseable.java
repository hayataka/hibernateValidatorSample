package com.github.hayataka.hibernatevalidatorsample.context;

import java.io.Closeable;

interface AutoCloseable extends Closeable {
	void close();
}
