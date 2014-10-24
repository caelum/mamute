package org.mamute.search;

import java.io.IOException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
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

@Controller
@ApplicationScoped
@CustomBrutauthRules(ModeratorOnlyRule.class)
public class IndexSyncJob {
	public static final Logger LOGGER = LoggerFactory.getLogger(IndexSyncJob.class);

	private QuestionIndex index;
	private Environment environment;
	private Linker linker;
	private Result result;
	private QuestionDAO questions;
	private HttpServletRequest request;


	@Deprecated
	protected IndexSyncJob() {
	}

	@Inject
	public IndexSyncJob(QuestionIndex index, Environment environment,
						Linker linker, Result result, QuestionDAO questions, HttpServletRequest request) {
		this.index = index;
		this.environment = environment;
		this.linker = linker;
		this.result = result;
		this.questions = questions;
		this.request = request;
	}

	@Post("/msdf0fhq924pqpsdl")
	public void indexSync() {
		linker.linkTo(this).execute();
		final HttpPost httpPost = new HttpPost(linker.get());
		final Cookie[] cookies = request.getCookies();
		LOGGER.info("Running thread to sync solr indexes");
		new Thread(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("Solr indexes thread started!");
				CookieStore cookieStore = new BasicCookieStore();
				for (Cookie c : cookies) {
					BasicClientCookie bc = new BasicClientCookie(c.getName(), c.getValue());
					bc.setDomain("localhost");
					cookieStore.addCookie(bc);
				}

				try (
						CloseableHttpClient httpClient = HttpClientBuilder.create()
								.setDefaultCookieStore(cookieStore)
								.build()
				) {
					CloseableHttpResponse response = httpClient.execute(httpPost);
					LOGGER.info("Index sync received " + response.getStatusLine().getStatusCode());
				} catch (IOException e) {
					LOGGER.info("Couldn't execute post " + httpPost);
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