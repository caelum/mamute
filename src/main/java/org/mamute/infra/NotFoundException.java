package org.mamute.infra;

public class NotFoundException extends RuntimeException {


	private static final long serialVersionUID = 1L;
	
	public NotFoundException() {
		super();
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(Exception e) {
		super(e);
	}
}
