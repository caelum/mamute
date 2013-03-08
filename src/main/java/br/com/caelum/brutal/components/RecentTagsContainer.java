package br.com.caelum.brutal.components;

import java.util.List;

import javax.annotation.PreDestroy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.model.TagUsage;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@ApplicationScoped
@Component
public class RecentTagsContainer {

	private List<TagUsage> recentTagsUsage;
	private final SessionFactory sf;
	
	public RecentTagsContainer(SessionFactory sf) {
		this.sf = sf;
		
	}

	public List<TagUsage> getRecentTagsUsage() {
		return recentTagsUsage;
	}
	
	public void execute() {
	    // we need to do this anyway, sorry
	    Session session = sf.openSession();
	    session.beginTransaction();
	    update(session);
	    session.getTransaction().commit();
	    session.close();
	}
	
	public void update(Session session) {
	    TagDAO tags = new TagDAO(session);
	    this.recentTagsUsage = tags.getRecentTagsSince(new DateTime().minusMonths(3));
	}
	
	@PreDestroy
	public void destroy() {
		sf.close();
	}
}
