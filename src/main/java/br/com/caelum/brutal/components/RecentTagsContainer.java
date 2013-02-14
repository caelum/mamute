package br.com.caelum.brutal.components;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
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
	
	@PostConstruct
	public void updateRecentTagsUsage() {
		Session session = sf.openSession();
		TagDAO tags = new TagDAO(session);
		this.recentTagsUsage = tags.getRecentTagsSince(new DateTime().minusMonths(3));
		session.close();
	}
	
	@PreDestroy
	public void destroy() {
		sf.close();
	}
}
