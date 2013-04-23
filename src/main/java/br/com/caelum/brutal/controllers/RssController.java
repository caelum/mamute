package br.com.caelum.brutal.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.infra.rss.RssFeedFactory;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RssController {
	private final RssFeedFactory rssFactory;
	private final QuestionDAO questions;
	private final HttpServletResponse response;
	private final Result result;

	public RssController(RssFeedFactory rssFactory, QuestionDAO questions, 
			HttpServletResponse response, Result result) {
		this.rssFactory = rssFactory;
		this.questions = questions;
		this.response = response;
		this.result = result;
	}
	
	@Path("/rss")
	public void rss() throws IOException {
		int maxResults = 30;
		List<Question> orderedByDate = questions.orderedByCreationDate(maxResults);
		OutputStream outputStream = response.getOutputStream();
		rssFactory.build(orderedByDate, outputStream);
		result.nothing();
	}
}
