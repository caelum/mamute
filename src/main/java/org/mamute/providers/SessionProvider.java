package org.mamute.providers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import java.util.concurrent.atomic.AtomicInteger;

/*
   If I declare this bean as SessionScoped I get this error:
   Bean declaring a passivating scope must be passivation capable.
*/
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
@RequestScoped
public class SessionProvider {
	private static final Logger logger  = LoggerFactory.getLogger(SessionProvider.class);
	private SessionFactory factory;
	private Session session;
	private static final AtomicInteger numberOfOpenSessions = new AtomicInteger(0);

	@Deprecated
	public SessionProvider() {

	}

	/**
	 * The class declares a constructor annotated @Inject
	 * No special declaration, such as an annotation, is required to define a managed bean.
	 * https://docs.oracle.com/javaee/6/tutorial/doc/gjfzi.html
	 * @param factory
	 */
	@Inject
	public SessionProvider(SessionFactory factory) {
		this.factory = factory;
	}

	@PostConstruct
	public void create()
	{
		session = this.factory.openSession();
		numberOfOpenSessions.incrementAndGet();
	}

	/**
	 * RequestScoped: A user’s interaction with a web application in a single HTTP request.
	 * https://docs.oracle.com/javaee/6/tutorial/doc/gjbbk.html
	 * @return
	 */
	@Produces
	@RequestScoped
	public Session getInstance() {
		return session;
	}

	/**
	 * CDI calls this method before starting to destroy the bean.
	 * https://docs.oracle.com/javaee/6/tutorial/doc/gmgkd.html
	 * @param session
	 */
	@PreDestroy
	public void close(@Disposes Session session)
	{
		// http://www.vraptor.org/en/docs/components/
		if (session.isOpen())
		{
			session.close();
		}
		logger.debug("number of open sessions = {}", numberOfOpenSessions.decrementAndGet());
	}
}
