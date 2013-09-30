package br.com.caelum.brutal.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.brutal.dao.NewsDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.infra.rss.write.RssFeedFactory;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.interfaces.RssContent;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;

@Controller
public class RssController {
	private static final int MAX_RESULTS = 30;
	@Inject private RssFeedFactory rssFactory;
	@Inject private QuestionDAO questions;
	@Inject private HttpServletResponse response;
	@Inject private Result result;
	@Inject private TagDAO tags;
	@Inject private NewsDAO news;
	@Inject private ResourceBundle bundle;
	
	@Get("/rss")
	public void rss() throws IOException {
		List<RssContent> orderedByDate = questions.orderedByCreationDate(MAX_RESULTS);
		String title = bundle.getString("questions.rss.title");
		String description = bundle.getString("questions.rss.description");
		buildRss(orderedByDate, title, description);
	}
	
	@Get("/rss/{tagName}")
	public void rss(String tagName) throws IOException {
		Tag tag = tags.findByName(tagName);
		if (tag == null) {
			result.notFound();
			return;
		}
		
		List<RssContent> orderedByDate = questions.orderedByCreationDate(MAX_RESULTS, tag);
		String title = bundle.getString("questions.rss.title");
		String description = bundle.getString("questions.rss.description");
		buildRss(orderedByDate, title, description);
	}

	@Get("/noticias/rss")
	public void newsRss() throws IOException {
		List<RssContent> orderedByDate = news.orderedByCreationDate(MAX_RESULTS);
		String title = bundle.getString("news.rss.title");
		String description = bundle.getString("news.rss.description");
		buildRss(orderedByDate, title, description);
	}
	
	private void buildRss(List<RssContent> orderedByDate, String title, String description) throws IOException {
		OutputStream outputStream = response.getOutputStream();
		response.setContentType("text/xml");
		rssFactory.build(orderedByDate, outputStream, title, description);
		result.nothing();
	}
}
