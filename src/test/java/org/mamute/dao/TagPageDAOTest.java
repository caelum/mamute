package org.mamute.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
		
		TagPage javaPage = tagPages.findByTag(java.getUriName());
		assertEquals(java.getName(), javaPage.getTag().getUriName());
	}

    @Test
    public void should_report_that_page_for_existing_tags_slugname_exists()
    {
        TagPageDAO tagPages = new TagPageDAO(session);
        Tag myTagWithPage = tag("myfavoritetag");
        Tag myTagWithoutPage = tag("myfavoritetag number two");
        session.save(myTagWithPage);
        session.save(myTagWithoutPage);

        tagPages.save(new TagPage(myTagWithPage, notMarked("aboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutaboutabout")));

        assertTrue(tagPages.existsOfTag(myTagWithPage.getUriName()));
        assertFalse(tagPages.existsOfTag(myTagWithoutPage.getUriName()));
    }
}
