package org.mamute.search;

public class IndexException extends RuntimeException {
	public IndexException() {
		super();
	}

	public IndexException(String msg) {
		super(msg);
	}

	public IndexException(String msg, Throwable t) {
		super(msg, t);
	}
}
