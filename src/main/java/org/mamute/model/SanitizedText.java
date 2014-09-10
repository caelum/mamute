package org.mamute.model;

public class SanitizedText {

	private String text;

	private SanitizedText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public static SanitizedText notSanitized(String text) {
		return new SanitizedText(text);
	}

	@Override
	public String toString() {
		return "SanitizedText [text=" + text + "]";
	}
	
}
