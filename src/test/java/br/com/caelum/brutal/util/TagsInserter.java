package br.com.caelum.brutal.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import br.com.caelum.brutal.model.Tag;

public class TagsInserter {

	private ScriptSessionFactoryCreator sessionProvider = new ScriptSessionFactoryCreator();
	private Session session;
    private static final Logger LOG = Logger.getLogger(TagsInserter.class); 

	
	public TagsInserter() {
		session = sessionProvider.getSession();
	}
	
	public static void main(String[] args) throws IOException {
		new TagsInserter().run();
		
	}

	private void run() throws IOException {
		session.beginTransaction();
		try {
			saveTags();
		    session.getTransaction().commit();
		} catch (Exception e) {
		    session.getTransaction().rollback();
		    throw e;
		}
		LOG.info("data import finished successfully");
	}

	private void saveTags() throws IOException {
		InputStream tagsStream = TagsInserter.class.getResourceAsStream("/tags");
		List<String> tags = Arrays.asList(IOUtils.toString(tagsStream).split(" "));
		for (String tag: tags) {
			LOG.info("inserting tag: "+ tag);
			if(doesNotExists(tag)){
				session.save(new Tag(tag, "", null));
			}
		}
	}

	private boolean doesNotExists(String tag) {
		return session.createQuery("from Tag t where t.name = :tag").setString("tag", tag).uniqueResult() == null;
	}

}
