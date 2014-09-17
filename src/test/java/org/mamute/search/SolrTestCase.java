package org.mamute.search;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.After;
import org.mamute.testcase.CDITestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SolrTestCase extends CDITestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(SolrTestCase.class);
	static SolrServer solrServer;

	static {
		//copy Solr home to target
		try {
			solrServer = cdiBasedContainer.instanceFor(SolrServer.class);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@After
	public void afterSolr() throws IOException, SolrServerException {
		solrServer.deleteByQuery("*:*");
	}

}
