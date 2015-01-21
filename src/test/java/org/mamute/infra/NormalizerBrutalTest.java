package org.mamute.infra;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mamute.infra.NormalizerBrutal;

public class NormalizerBrutalTest {


	@Test
	public void should_remove_signs_from_any_string() {
		String text = "áéêõäũàÁÓõÒ";
		String slug = NormalizerBrutal.toSlug(text);
		assertEquals("aeeoauaaooo", slug);

		String slugEncoded = NormalizerBrutal.toSlug(text, true);
		assertEquals("aeeoauaaooo", slugEncoded);
	}
	
	@Test
	public void should_replace_spaces_with_dashes() {
		String text = "como faz para normalizar uma string?";
		String slug = NormalizerBrutal.toSlug(text);
		assertEquals("como-faz-para-normalizar-uma-string", slug);

		String slugEncoded = NormalizerBrutal.toSlug(text, true);
		assertEquals("como-faz-para-normalizar-uma-string%3f", slugEncoded);
	}
	
	@Test
	public void should_remove_question_marks_and_bangs() {
		String text = "como faz para normalizar uma string?!";
		String slug = NormalizerBrutal.toSlug(text);
		assertEquals("como-faz-para-normalizar-uma-string", slug);

		String slugEncoded = NormalizerBrutal.toSlug(text, true);
		assertEquals("como-faz-para-normalizar-uma-string%3f%21", slugEncoded);
	}
	
	@Test
	public void shouldLowerCase() {
		String text = "COMO FAZ PARA NORMALIZAR UMA STRING?!";
		String slug = NormalizerBrutal.toSlug(text);
		assertEquals("como-faz-para-normalizar-uma-string", slug);

		String slugEncoded = NormalizerBrutal.toSlug(text, true);
		assertEquals("como-faz-para-normalizar-uma-string%3f%21", slugEncoded);
	}


	@Test
	public void should_url_encode_special_chars_if_requested() {
		String text = "ČŽŠ#+";

		String slug = NormalizerBrutal.toSlug(text);
		assertEquals("czs", slug);

		String slugEncoded = NormalizerBrutal.toSlug(text, true);
		assertEquals("czs%23%2b", slugEncoded);
	}
}
