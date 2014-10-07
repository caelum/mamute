package org.mamute.search;

import java.io.IOException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.mamute.brutauth.auth.rules.ModeratorOnlyRule;
import org.mamute.dao.QuestionDAO;
import org.mamute.model.Question;
import org.mamute.vraptor.Linker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.events.VRaptorInitialized;

@Controller @ApplicationScoped
@CustomBrutauthRules(ModeratorOnlyRule.class)
public class IndexSyncJob {
	public static final Logger LOGGER = LoggerFactory.getLogger(IndexSyncJob.class);

	private QuestionIndex index;
	private Environment environment;
	private Linker linker;
	private Result result;
	private QuestionDAO questions;


	@Deprecated
	protected IndexSyncJob() {
	}

	@Inject
	public IndexSyncJob(QuestionIndex index, Environment environment,
			Linker linker, Result result, QuestionDAO questions) {
		this.index = index;
		this.environment = environment;
		this.linker = linker;
		this.result = result;
		this.questions = questions;
	}
	
	@Post("/msdf0fhq924pqpsdl")
	public void indexSync() {
		linker.linkTo(this).execute();
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
		
	@Post("/akwiqeojovndfasf0asf0s9ad8fas9d")
	public void execute() {
			long pages = questions.numberOfPages();
			long total = 0;
			LOGGER.info("Syncing questions!");
			for (int i = 0; i < pages; i++) {
				List<Question> q = questions.allVisible(i);
				index.indexQuestionBatch(q);
				total += q.size();
			}
			LOGGER.info("Synced " + total + " questions");
			result.nothing();
	}

	@Post("/akwiqeojovndfasf0asf0s9ad8fas9d12io3nwo120")
	public void onStartup(@Observes VRaptorInitialized init) {
		if (environment.supports("solr.syncOnStartup") && environment.supports("feature.solr")) {
			indexSync();
		}
	}
}