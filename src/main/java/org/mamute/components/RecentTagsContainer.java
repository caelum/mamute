package org.mamute.components;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.environment.Environment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.joda.time.DateTime;
import org.mamute.dao.TagDAO;
import org.mamute.model.TagUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class RecentTagsContainer {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(RecentTagsContainer.class);

	private List<TagUsage> recentTagsUsage;
	@Inject private SessionFactory sf;
	@Inject private Environment env;

	public List<TagUsage> getRecentTagsUsage() {
		return recentTagsUsage;
	}
	
	public void execute() {
	    // we need to do because of this class is app scoped
	    Session session = sf.openSession();
	    session.beginTransaction();
	    try {
			update(session);
		} catch (SQLGrammarException ex) {
			ignoreIfTableDidNotExist(ex);
		}
	    session.getTransaction().commit();
	    session.close();
	}

	private void ignoreIfTableDidNotExist(SQLGrammarException ex) {
		if(ex.getCause().getMessage().contains(".question' doesn't exist")) {
			// ignore if its the first time we are running and the table still does not exist
			LOGGER.warn("Unable to run the mysql query to update the recent tags", ex);
		} else {
			// nasty catch and retrow, sorry.
			throw ex;
		}
	}
	
	public void update(Session session) {
	    TagDAO tags = new TagDAO(session);
		int maxRecentTags = env.has("max_recent_tags") ? Integer.parseInt(env.get("max_recent_tags")) : 10;
	    this.recentTagsUsage = tags.getRecentTagsSince(new DateTime().minusMonths(3), maxRecentTags);
	}
	
	public void destroy() {
		sf.close();
	}
}
