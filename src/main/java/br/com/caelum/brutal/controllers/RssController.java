package br.com.caelum.brutal.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.infra.rss.RssFeedFactory;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RssController {
	private final RssFeedFactory rssFactory;
	private final QuestionDAO questions;
	private final HttpServletResponse response;
	private final Result result;
	private final TagDAO tags;
	private static final int MAX_RESULTS = 30;

	public RssController(RssFeedFactory rssFactory, QuestionDAO questions, 
			HttpServletResponse response, Result result, TagDAO tags) {
		this.rssFactory = rssFactory;
		this.questions = questions;
		this.response = response;
		this.result = result;
		this.tags = tags;
	}
	
	@Get("/rss")
	public void rss() throws IOException {
		List<Question> orderedByDate = questions.orderedByCreationDate(MAX_RESULTS);
		buildRss(orderedByDate);
	}
	@Get("/rss/{tagName}")
	public void rss(String tagName) throws IOException {
		Tag tag = tags.findByName(tagName);
		if (tag == null) {
			result.notFound();
			return;
		}
		
		List<Question> orderedByDate = questions.orderedByCreationDate(MAX_RESULTS, tag);
		buildRss(orderedByDate);
	}

	private void buildRss(List<Question> orderedByDate) throws IOException {
		OutputStream outputStream = response.getOutputStream();
		response.setContentType("text/xml");
		rssFactory.build(orderedByDate, outputStream);
		result.nothing();
	}
}
