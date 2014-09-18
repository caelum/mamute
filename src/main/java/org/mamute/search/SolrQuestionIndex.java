package org.mamute.search;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;
import javax.enterprise.inject.Vetoed;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Vetoed
public class SolrQuestionIndex implements QuestionIndex {
	private static final Logger LOGGER = LoggerFactory.getLogger(SolrQuestionIndex.class);

	private SolrServer server;

	public SolrQuestionIndex(SolrServer server) {
		this.server = server;
	}

	@Override
	public void indexQuestion(Question question) {
		try {
			SolrInputDocument doc = toDoc(question);
			server.add(doc);
			server.commit();
			LOGGER.info("Question synced or updated: " + question);
		} catch (IOException | SolrServerException e) {
			throw new IndexException("Could not index Question [" + question.getId() + "]", e);
		}
	}

	@Override
	public void indexQuestionBatch(Collection<Question> questions) {
		try {
			List<SolrInputDocument> docs = new ArrayList<>();
			for (Question question : questions) {
				docs.add(toDoc(question));
				LOGGER.info("Question synced or updated(trying to): " + question);
				
			}
			server.add(docs);
			server.commit();
			LOGGER.info("Questions synced or updated with success");
			
		} catch (IOException | SolrServerException e) {
			List<Long> ids = Lists.transform(new ArrayList<Question>(questions), new Function<Question, Long>() {
				public Long apply(Question input) {
					return input.getId();
				}
			});
			throw new IndexException("Could not index a Question in the following list: " + ids, e);
		}
	}

	@Override
	public List<Long> find(String query, int maxResults) {
		try {
			query = URLEncoder.encode(query, "utf-8");
			return query("description:" + query + " OR (title:" + query + ")^1.5 OR tags:" + query, maxResults);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Could not encode query "+ query, e);
		}
	}

	private SolrInputDocument toDoc(Question q) {
		List<String> tagNames = Lists.transform(q.getTags(), new Function<Tag, String>() {
			@Nullable
			@Override
			public String apply(@Nullable Tag tag) {
				return tag.getName();
			}
		});

		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", q.getId());
		doc.addField("title", q.getTitle());
		doc.addField("description", q.getMarkedDescription());
		doc.addField("tags", tagNames.toString().replace("[", "").replace("]", ""));
		return doc;
	}

	private List<Long> query(String queryString, int maxResults) {
		LOGGER.debug(queryString);
		SolrQuery query = new SolrQuery(queryString).setRows(maxResults);
		QueryResponse rsp;
		try {
			rsp = server.query(query);
		} catch (SolrServerException e) {
			throw new IndexException("Could not query from index", e);
		}
		SolrDocumentList docs = rsp.getResults();

		List<Long> ids = new ArrayList<>();
		for (SolrDocument doc : docs) {
			ids.add(Long.parseLong((String) doc.getFieldValue("id")));
		}
		return ids;
	}

}
