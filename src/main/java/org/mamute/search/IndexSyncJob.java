package org.mamute.search;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.events.InterceptorsReady;
import br.com.caelum.vraptor.events.VRaptorInitialized;
import br.com.caelum.vraptor.quartzjob.CronTask;
import org.mamute.dao.QuestionDAO;
import org.mamute.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;

@Controller
public class IndexSyncJob implements CronTask {
	public static final Logger LOGGER = LoggerFactory.getLogger(IndexSyncJob.class);
	public static final String DEFAULT_SYNC = "0 0 0/1 1/1 * ? *";

	private static String frequency;

	@Inject private Result result;
	@Inject private QuestionDAO questions;
	@Inject private QuestionIndex index;
	@Inject private Environment environment;

	@Deprecated
	protected IndexSyncJob() {
		LOGGER.info("Instance");
	}

	@Override
	@Post("/sdfajsdfjaoiji")
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

	@Override
	public String frequency() {
		return frequency;
	}

	public void onStartup(@Observes InterceptorsReady init) {
		if (Boolean.parseBoolean(environment.get("solr.syncOnStartup"))) {
			execute();
		}
	}

	/**
	 * Grabs the frequency of the job (bit of a hack to get things in the
	 * correct scope)
	 */
	@ApplicationScoped
	static class FrequencyInit {
		@Inject private Environment environment;

		public void onStartup(@Observes VRaptorInitialized init) {
			frequency = environment.get("solr.syncJob", DEFAULT_SYNC);
		}
	}

}