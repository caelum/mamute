package br.com.caelum.brutal.sanitizer;

public class QuotesSanitizer {

	public static String sanitize(String string){
		return string.replaceAll("\"","");
	}
}
