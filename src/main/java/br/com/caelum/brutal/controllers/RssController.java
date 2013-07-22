package br.com.caelum.brutal.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.brutal.dao.NewsDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.infra.rss.RssFeedFactory;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.interfaces.RssContent;
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
	private NewsDAO news;

	public RssController(RssFeedFactory rssFactory, QuestionDAO questions, 
			HttpServletResponse response, Result result, TagDAO tags, NewsDAO news) {
		this.rssFactory = rssFactory;
		this.questions = questions;
		this.response = response;
		this.result = result;
		this.tags = tags;
		this.news = news;
	}
	
	@Get("/rss")
	public void rss() throws IOException {
		List<RssContent> orderedByDate = questions.orderedByCreationDate(MAX_RESULTS);
		buildRss(orderedByDate);
	}
	@Get("/rss/{tagName}")
	public void rss(String tagName) throws IOException {
		Tag tag = tags.findByName(tagName);
		if (tag == null) {
			result.notFound();
			return;
		}
		
		List<RssContent> orderedByDate = questions.orderedByCreationDate(MAX_RESULTS, tag);
		buildRss(orderedByDate);
	}

	@Get("/noticias/rss")
	public void newsRss() throws IOException {
		List<RssContent> orderedByDate = news.orderedByCreationDate(MAX_RESULTS);
		buildRss(orderedByDate);
	}
	
	private void buildRss(List<RssContent> orderedByDate) throws IOException {
		OutputStream outputStream = response.getOutputStream();
		response.setContentType("text/xml");
		rssFactory.build(orderedByDate, outputStream);
		result.nothing();
	}
}
