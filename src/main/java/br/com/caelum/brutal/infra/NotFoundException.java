package br.com.caelum.brutal.infra;

public class NotFoundException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(IllegalArgumentException e) {
		super(e);
	}
}
