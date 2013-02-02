package br.com.caelum.brutal.dao;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integracao.dao.DatabaseTestCase;
import br.com.caelum.brutal.model.Tag;


public class TagDAOTest extends DatabaseTestCase{

	private TagDAO tags;


	@Before
	public void setup() {
		this.tags = new TagDAO(session);
	}
	
	@Test
	public void should_save_if_does_not_exists_on_database() {
		Tag tag = new Tag("rails", "", null);
		Tag persistedTag = tags.saveOrLoad(tag);
		assertEquals(tag, persistedTag);
	}
	
	@Test
	public void should_load_if_exists_on_database() {
		String savedTagDescription = "for rails related questions";
		Tag rails = new Tag("rails", savedTagDescription, null);
		tags.saveOrLoad(rails);
		Tag otherRailsTag = new Tag("rails", "", null);
		Tag persistedTag = tags.saveOrLoad(otherRailsTag);
		assertEquals(rails, persistedTag);
		assertEquals(savedTagDescription, persistedTag.getDescription());
	}

}
