package br.com.caelum.brutal.controllers.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import br.com.caelum.brutal.model.Tag;
import br.com.caelum.vraptor.Post;

public class TagsInserterController {
	
	private final Session session;
	private final Logger LOG = Logger.getLogger(TagsInserterController.class);

	public TagsInserterController(Session session) {
		this.session = session;
	}
	
	@Post("/asdnajungOUNGTwnuitna")
	public void saveTags() throws IOException {
		InputStream tagsStream = TagsInserterController.class.getResourceAsStream("/tags");
		List<String> tagsList = Arrays.asList(IOUtils.toString(tagsStream).split(" "));
		for (String tag: tagsList) {
			LOG.info("trying to insert tag: "+tag);
			if(doesNotExists(tag)){
				LOG.info("inserting tag: "+tag);
				session.save(new Tag(tag, "", null));
			}else{
				LOG.warn("tag already exists: "+tag);
			}
		}
		LOG.info("done");
	}

	private boolean doesNotExists(String tag) {
		return session.createQuery("from Tag t where t.name = :tag").setString("tag", tag).uniqueResult() == null;
	}
}
