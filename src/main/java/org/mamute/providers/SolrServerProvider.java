package org.mamute.providers;

import br.com.caelum.vraptor.environment.Environment;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.core.CoreContainer;
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

import java.io.File;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class SolrServerProvider {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SolrServerProvider.class);

	private SolrServer server;
	private final Environment env;

	@Inject
	public SolrServerProvider(Environment env) {
		this.env = env;
	}

	@PostConstruct
	public void create() {
		//embedded options
		Boolean embedded = Boolean.parseBoolean(env.get("solr.embedded", "false"));
		String solrHome = env.get("solr.home", "");
		String solrCore = env.get("solr.core", "");

		//remote options
		String remoteUrl = env.get("solr.url", "");

		if (embedded && isNotEmpty(solrHome)) {
			LOGGER.info("Starting embedded Solr");
			CoreContainer coreContainer = new CoreContainer(new File(solrHome).getAbsolutePath());
			coreContainer.load();
			server = new EmbeddedSolrServer(coreContainer, solrCore);
		} else if (isNotEmpty(remoteUrl)) {
			LOGGER.info("Connecting to external Solr at [" + remoteUrl + "]");
			server = new HttpSolrServer(remoteUrl);
		} else {
			LOGGER.warn("Solr config options not valid, not initting");
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
