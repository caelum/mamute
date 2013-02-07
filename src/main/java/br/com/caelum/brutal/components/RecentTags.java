package br.com.caelum.brutal.components;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.model.TagUsage;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@ApplicationScoped
@Component
public class RecentTags {

	private List<TagUsage> recentTagsSince;
	private final SessionFactory sf;
	
	public RecentTags(SessionFactory sf) {
		this.sf = sf;
	}
	

	public List<TagUsage> getRecentTagsSince() {
		return recentTagsSince;
	}
	
	@PostConstruct
	public void updateRecentTags() {
		Logger logger = Logger.getLogger(getClass());
		logger.warn("instanciando a bagaça");
		Session session = sf.openSession();
		TagDAO tags = new TagDAO(session);
		this.recentTagsSince = tags.getRecentTagsUsageSince(DateTime.now().minusMonths(3));
		session.close();
	
	}
	
	@PreDestroy
	public void destroy() {
		sf.close();
	}
}
