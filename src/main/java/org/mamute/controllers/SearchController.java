package org.mamute.controllers;

import static org.mamute.sanitizer.HtmlSanitizer.sanitize;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.mamute.dao.QuestionDAO;
import org.mamute.model.Question;
import org.mamute.sanitizer.HtmlSanitizer;
import org.mamute.search.QuestionIndex;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.view.Results;

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
	private final static Logger LOG = Logger.getLogger(SearchController.class);;

	
	
	@Get("/search")
	public void search(String query) {
		boolean supportsSolr = env.supports("feature.solr");
		boolean supportsGoogleSearch = env.supports("feature.google_search");
		
		if(supportsSolr && supportsGoogleSearch)
			throw new IllegalArgumentException("both feature.solr and feature.google_search are setted to true on the curren environment, you need to set one of them to false");

		if(!supportsSolr && !supportsGoogleSearch) {
			LOG.debug("The action /search is only available when feature.solr=true or feature.google_search=true on current environment");
			result.notFound();
			return;
		}
		
		result.include("query", sanitize(query));
		
		if(supportsSolr) {
			result.include("results", doSearch(query));
			result.forwardTo("/WEB-INF/jsp/search/solrResult.jsp");
		}
		
		if(supportsGoogleSearch) {
			result.include("customGoogleSearchKey", env.get("custom_google_search_key"));
			result.forwardTo("/WEB-INF/jsp/search/googleSearchResult.jsp");
		}

	}

	@Get("/searchAjax")
	public void searchAjax(String query) {
		boolean doesntSupportSolr = !env.supports("feature.solr");
		if(doesntSupportSolr) {
			LOG.debug("The action /searchAjax is only available when feature.solr=true on current environment");
			result.notFound();
			return;
		}
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
