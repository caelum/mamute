package org.mamute.search;

import com.google.common.io.Resources;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.hibernate.Session;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.mamute.dao.DatabaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public abstract class SolrTestCase extends DatabaseTestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(SolrTestCase.class);

	static SolrServer solrServer;
	static Session hibernateSession;

	static {
		//copy Solr home to target
		try {
			FileUtils.copyDirectory(new File(Resources.getResource("solr").getPath()), new File("target/solr"));
			solrServer = cdiBasedContainer.instanceFor(SolrServer.class);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@BeforeClass
	public static void openSession() {
		hibernateSession = factory.openSession();
		hibernateSession.beginTransaction();
	}

}
