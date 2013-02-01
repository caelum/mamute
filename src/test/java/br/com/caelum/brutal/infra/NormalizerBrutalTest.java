package br.com.caelum.brutal.infra;

import static org.junit.Assert.*;

import org.junit.Test;

public class NormalizerBrutalTest {


	@Test
	public void shouldRemoveSignsFromAnyString() {
		String text = "áéêõäũàÁÓõÒ";
		String slug = NormalizerBrutal.toSlug(text);
		assertEquals("aeeoauaaooo", slug);
	}
	
	@Test
	public void shouldReplaceSpacesWithDashes() {
		String text = "como faz para normalizar uma string?";
		String slug = NormalizerBrutal.toSlug(text);
		assertEquals("como-faz-para-normalizar-uma-string", slug);
	}
	
	@Test
	public void shouldRemoveQuestionMarksAndBangs() {
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
