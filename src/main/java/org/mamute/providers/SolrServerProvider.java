package org.mamute.providers;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.mamute.vraptor.environment.MamuteEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class SolrServerProvider {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SolrServerProvider.class);

	SolrServer server;
	MamuteEnvironment env;

	@Inject
	public SolrServerProvider(MamuteEnvironment env) {
		this.env = env;
	}

	@PostConstruct
	public void create() {
		LOGGER.info("Connecting to localhost");
		if (env.has("solr.url")) {
			server = new HttpSolrServer(env.get("solr.url"));
		}
	}

	@Produces
	@ApplicationScoped
	public SolrServer getInstance() {
		return server;
	}

	public void destroy(@Disposes SolrServer server) {

	}
}
