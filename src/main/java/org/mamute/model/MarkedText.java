package org.mamute.model;

public class MarkedText {

	private String pure;
	private String marked;

	public static MarkedText pureAndMarked(String pure, String marked) {
		return new MarkedText(pure, marked);
	}

	public static MarkedText notMarked(String pure) {
		return new MarkedText(pure, pure);
	}

	private MarkedText(String pure, String marked) {
		this.pure = pure;
		this.marked = marked;
	}
	
	public String getPure() {
		return pure;
	}

	public String getMarked() {
		return marked;
	}

	@Override
	public String toString() {
		return marked;
	}


	
}
