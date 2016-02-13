package org.mamute.sanitizer;

public class QuotesSanitizer {

	public static String sanitize(String string){
		return string.replaceAll("\"","");
	}
}
