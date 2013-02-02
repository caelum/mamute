package br.com.caelum.brutal.infra;

import static org.junit.Assert.*;

import org.junit.Test;

public class NormalizerBrutalTest {


	@Test
	public void should_remove_signs_from_any_string() {
		String text = "áéêõäũàÁÓõÒ";
		String slug = NormalizerBrutal.toSlug(text);
		assertEquals("aeeoauaaooo", slug);
	}
	
	@Test
	public void should_replace_spaces_with_dashes() {
		String text = "como faz para normalizar uma string?";
		String slug = NormalizerBrutal.toSlug(text);
		assertEquals("como-faz-para-normalizar-uma-string", slug);
	}
	
	@Test
	public void should_remove_question_marks_and_bangs() {
		String text = "como faz para normalizar uma string?!";
		String slug = NormalizerBrutal.toSlug(text);
		assertEquals("como-faz-para-normalizar-uma-string", slug);
	}
	
	@Test
	public void shouldLowerCase() {
		String text = "COMO FAZ PARA NORMALIZAR UMA STRING?!";
		String slug = NormalizerBrutal.toSlug(text);
		assertEquals("como-faz-para-normalizar-uma-string", slug);
	}

}
