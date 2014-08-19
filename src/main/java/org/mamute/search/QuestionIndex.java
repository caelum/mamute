package org.mamute.search;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.mamute.dao.QuestionDAO;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionIndex {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionIndex.class);
	public static final String QUESTION_ROOT = "type:" + Question.class.getCanonicalName() + " && ";
	private SolrServer server;
	private QuestionDAO dao;

	@Inject
	public QuestionIndex(SolrServer server, QuestionDAO dao) {
		this.server = server;
		this.dao = dao;
	}

	public void indexQuestion(Question q) {
		try {
			SolrInputDocument doc = toDoc(q);
			server.add(doc);
			server.commit();
		} catch (IOException | SolrServerException e) {
			throw new IndexException("Could not index Question", e);
		}
	}

	public void indexQuestionBatch(List<Question> questions) {
		try {
			List<SolrInputDocument> docs = new ArrayList<>();
			for (Question q : questions) {
				docs.add(toDoc(q));
			}
			server.add(docs);
			server.commit();
		} catch (IOException | SolrServerException e) {
			throw new IndexException("Could not index Question", e);
		}
	}

	public List<Question> findQuestionsByTitle(String title, int maxResults) {
		return query(QUESTION_ROOT + "title:" + title, maxResults);
	}

	public List<Question> findQuestionsByTitleAndTag(String title, List<Tag> tags, int maxResults) {
		String tagQuery = Joiner.on(" OR ").join(Lists.transform(tags, new Function<Tag, Object>() {
			public Object apply(Tag tag) {
				return String.format("tags: %s^2", tag.getName());
			}
		}));

		return query(QUESTION_ROOT + "title:" + title + " " + tagQuery, maxResults);
	}

	SolrInputDocument toDoc(Question q) {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", q.getId());
		doc.addField("title", q.getTitle());
		doc.addField("tags", Lists.transform(q.getTags(), new Function<Tag, String>() {
			@Nullable
			@Override
			public String apply(@Nullable Tag tag) {
				return tag.getName();
			}
		}));
		doc.addField("type", Question.class.getCanonicalName());
		return doc;
	}

	List<Question> query(String queryString, int maxResults) {
		LOGGER.debug(queryString);
		SolrQuery query = new SolrQuery(queryString).setRows(maxResults);
		QueryResponse rsp;
		try {
			rsp = server.query(query);
		} catch (SolrServerException e) {
			throw new IndexException("Could not read Question from index", e);
		}
		SolrDocumentList docs = rsp.getResults();

		List<Question> questions = new ArrayList<>();
		for (SolrDocument doc : docs) {
			Long id = Long.parseLong((String) doc.getFieldValue("id"));
			Question q = dao.getById(id);
			if (q != null) {
				questions.add(q);
			}
		}
		return questions;
	}

}
