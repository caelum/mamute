package org.mamute.infra;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

public class NormalizerBrutal {
	public static String toSlug(String input, boolean urlEncodeNonNormalizableCharacter)  {
		if(input == null || input.isEmpty()) return "";

		String nonLatinPattern = urlEncodeNonNormalizableCharacter ? "[^\\P{M}]" : "[^\\w-]";

		Pattern NONLATIN = Pattern.compile(nonLatinPattern);
		Pattern WHITESPACE = Pattern.compile("[\\s]");
		String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
		String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
		String slug = NONLATIN.matcher(normalized).replaceAll("");

		if (urlEncodeNonNormalizableCharacter) {
			try {
				slug = URLEncoder.encode(slug, "UTF-8");
			} catch (UnsupportedEncodingException e) {	}
		}

		return slug.toLowerCase(Locale.ENGLISH);
	}

	public static String toSlug(String input)  {
		return toSlug(input, false);
	}
}
