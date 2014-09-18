package org.mamute.controllers;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.mamute.brutauth.auth.rules.ModeratorOnlyRule;
import org.mamute.dao.QuestionDAO;
import org.mamute.environment.EnvironmentDependent;
import org.mamute.model.Question;
import org.mamute.search.QuestionIndex;
import org.mamute.vraptor.Linker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;

@Controller
@EnvironmentDependent(supports="feature.solr")
public class SolrIndexController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SolrIndexController.class);

	@Inject private Result result;
	@Inject private QuestionIndex index;
	@Inject private QuestionDAO dao;
	@Inject private Linker linker;
	
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	@Post("/msdf0fhq924pqpsdl")
	public void indexSync() {
		linker.linkTo(this).executeInFact();
		final PostMethod postMethod = new PostMethod(linker.get());
		LOGGER.info("Running thread to sync solr indexes");
		new Thread(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("Solr indexes thread started!");
				HttpClient httpClient = new HttpClient();
				try {
					httpClient.executeMethod(postMethod);
				} catch (IOException e) {
					LOGGER.debug("Couldn't execute post "+postMethod);
				}
			}
		}).start();
		result.nothing();
	}
	
	@Post("/sdfajsdfjaoiji")
	public void executeInFact() {
		long pages = dao.numberOfPages();
		long total = 0;
		LOGGER.info("Syncing questions!");
		for (int i = 0; i < pages; i++) {
			List<Question> q = dao.allVisible(i);
			index.indexQuestionBatch(q);
			total += q.size();
		}
		LOGGER.info("Synced " + total + " questions");
		result.nothing();
	}

}
