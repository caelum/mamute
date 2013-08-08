package br.com.caelum.brutal.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.TagPage;

public class TagPageDAOTest extends DatabaseTestCase{

	@Test
	public void should_get_tag_page_by_tag() {
		TagPageDAO tagPages = new TagPageDAO(session);
		Tag java = tag("java");
		session.save(java);
		tagPages.save(new TagPage(java, "aboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutabout"));
		
		TagPage javaPage = tagPages.findByTag(java.getName());
		assertEquals(java.getName(), javaPage.getTagName());
	}

}
