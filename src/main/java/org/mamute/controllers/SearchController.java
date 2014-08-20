package org.mamute.controllers;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import org.mamute.dao.QuestionDAO;
import org.mamute.model.Question;
import org.mamute.sanitizer.HtmlSanitizer;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.routes.annotation.Routed;
import org.mamute.search.QuestionIndex;

import java.util.ArrayList;
import java.util.List;

@Routed
@Controller
public class SearchController {

	@Inject
	private Result result;
	@Inject
	private Environment env;
	@Inject
	private QuestionIndex index;
	@Inject
	private QuestionDAO dao;

	@Get
	public void search(String query) {
		String sanitized = HtmlSanitizer.sanitize(query);
		result.include("customGoogleSearchKey", env.get("custom_google_search_key"));
		result.include("query", sanitized);
	}

	@Get
	public void indexSearch(String query) {
		String sanitized = HtmlSanitizer.sanitize(query);
		List<Long> ids = index.findQuestionsByTitle(sanitized, 10);
		List<Question> questions = new ArrayList<>();
		for (Long id : ids) {
			questions.add(dao.getById(id));
		}

		result.include("results", questions);
		result.include("query", sanitized);
	}
}
