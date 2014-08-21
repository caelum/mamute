package org.mamute.controllers;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import org.mamute.dao.QuestionDAO;
import org.mamute.model.Question;
import org.mamute.sanitizer.HtmlSanitizer;

import br.com.caelum.vraptor.environment.Environment;
import org.mamute.search.QuestionIndex;

import java.util.List;

@Controller
public class SearchController {

	@Inject
	private Result result;
	@Inject
	private Environment env;
	@Inject
	private QuestionIndex index;
	@Inject
	private QuestionDAO questions;

	@Get("/search")
	public void search(String query) {
		result.include("results", doSearch(query));
		result.include("query", HtmlSanitizer.sanitize(query));
	}

	@Get("/searchAjax")
	public void searchAjax(String query) {
		result.use(Results.json())
				.from(doSearch(query))
				.include(
						"information",
						"information.tags"
				).exclude(
				"information.comment",
				"information.description",
				"information.markedDescription",
				"information.status",
				"information.ip",
				"information.tags.author",
				"information.tags.usageCount"
		).serialize();
	}

	private List<Question> doSearch(String query) {
		String sanitized = HtmlSanitizer.sanitize(query);
		List<Long> ids = index.findQuestionsByTitle(sanitized, 10);
		return questions.getByIds(ids);
	}
}
