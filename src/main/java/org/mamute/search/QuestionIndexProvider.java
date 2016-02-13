package org.mamute.search;

import br.com.caelum.vraptor.environment.Environment;
import org.apache.solr.client.solrj.SolrServer;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static java.lang.Boolean.parseBoolean;

public class QuestionIndexProvider {

	private Environment environment;
	private Instance<SolrServer> server;

	@Inject
	public QuestionIndexProvider(Environment environment, Instance<SolrServer> server) {
		this.environment = environment;
		this.server = server;
	}

	@Produces
	public QuestionIndex build() {
		boolean useSolr = parseBoolean(environment.get("feature.solr", "false"));
		if (useSolr) {
			return new SolrQuestionIndex(server.iterator().next());
		}
		return new NullQuestionIndex();
	}
}
