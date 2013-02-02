package br.com.caelum.brutal.model;

public enum VoteType {
	UP(1), DOWN(-1);

	private final int value;

	private VoteType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
