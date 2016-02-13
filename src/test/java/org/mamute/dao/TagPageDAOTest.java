package org.mamute.dao;

import static org.junit.Assert.assertEquals;
import static org.mamute.model.MarkedText.notMarked;

import org.junit.Test;
import org.mamute.dao.TagPageDAO;
import org.mamute.model.MarkedText;
import org.mamute.model.Tag;
import org.mamute.model.TagPage;

public class TagPageDAOTest extends DatabaseTestCase{

	@Test
	public void should_get_tag_page_by_tag() {
		TagPageDAO tagPages = new TagPageDAO(session);
		Tag java = tag("java");
		session.save(java);
		tagPages.save(new TagPage(java, notMarked("aboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutabout")));
		
		TagPage javaPage = tagPages.findByTag(java.getName());
		assertEquals(java.getName(), javaPage.getTagName());
	}

}
