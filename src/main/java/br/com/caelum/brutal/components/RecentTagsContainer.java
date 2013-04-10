package br.com.caelum.brutal.components;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.model.TagUsage;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@ApplicationScoped
@Component
public class RecentTagsContainer {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(RecentTagsContainer.class);

	private List<TagUsage> recentTagsUsage;
	private final SessionFactory sf;
	
	public RecentTagsContainer(SessionFactory sf) {
		this.sf = sf;
	}

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
	    this.recentTagsUsage = tags.getRecentTagsSince(new DateTime().minusMonths(3));
	}
	
	public void destroy() {
		sf.close();
	}
}
