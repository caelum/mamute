package org.mamute.controllers;

import static org.mamute.sanitizer.HtmlSanitizer.sanitize;

import java.util.List;

import javax.inject.Inject;

import org.mamute.dao.QuestionDAO;
import org.mamute.environment.EnvironmentDependent;
import org.mamute.model.Question;
import org.mamute.sanitizer.HtmlSanitizer;
import org.mamute.search.QuestionIndex;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;

@Controller
@EnvironmentDependent(supports = "feature.solr")
public class SolrSearchController {

	@Inject
	private Result result;
	@Inject
	private QuestionIndex index;
	@Inject
	private QuestionDAO questions;

	@Get("/search")
	public void search(String query) {
		result.include("query", sanitize(query));
		result.include("results", doSearch(query, 10));
	}

	@Get("/questionSuggestion")
	public void questionSuggestion(String query, int limit) {
		result.forwardTo(BrutalTemplatesController.class).questionSuggestion(query, doSearch(query, limit));
	}

	private List<Question> doSearch(String query, int limit) {
		String sanitized = HtmlSanitizer.sanitize(query);
		List<Long> ids = index.findQuestionsByTitle(sanitized, limit);
		return questions.getByIds(ids);
	}
}
