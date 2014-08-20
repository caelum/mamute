package org.mamute.controllers;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import org.mamute.brutauth.auth.rules.ModeratorOnlyRule;
import org.mamute.dao.QuestionDAO;
import org.mamute.model.Question;
import org.mamute.search.QuestionIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

@Controller
public class IndexController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

	@Inject
	private Result result;
	@Inject
	private QuestionIndex index;
	@Inject
	private QuestionDAO dao;

	@CustomBrutauthRules(ModeratorOnlyRule.class)
	@Post("/msdf0fhq924pqpsdl")
	public void indexSync() {
		long pages = dao.numberOfPages();
		long total = 0;
		for (int i = 0; i < pages; i++) {
			List<Question> q = dao.allVisible(i);
			index.indexQuestionBatch(q);
			total += q.size();
		}

		LOGGER.info("Synced " + total + " questions");
		result.nothing();
	}

}
