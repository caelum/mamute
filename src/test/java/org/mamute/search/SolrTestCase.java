package org.mamute.search;

import org.apache.solr.client.solrj.SolrServer;
import org.hibernate.Session;
import org.junit.BeforeClass;
import org.mamute.dao.DatabaseTestCase;

public class SolrTestCase extends DatabaseTestCase {
	static SolrServer solrServer;
	static Session hibernateSession;

	static {
		solrServer = cdiBasedContainer.instanceFor(SolrServer.class);
	}

	@BeforeClass
	public static void openSession(){
		hibernateSession = factory.openSession();
		hibernateSession.beginTransaction();
	}

}
