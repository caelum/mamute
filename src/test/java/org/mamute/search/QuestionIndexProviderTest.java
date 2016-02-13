package org.mamute.search;

import br.com.caelum.vraptor.environment.Environment;
import org.apache.solr.client.solrj.SolrServer;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import javax.enterprise.inject.Instance;

import java.util.Iterator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestionIndexProviderTest {

	@Test
	public void should_build_null_question_index() throws Exception {
		Environment env = mock(Environment.class);
		when(env.get(eq("feature.solr"), anyString())).thenReturn("false");
		Instance<SolrServer> instance = mock(Instance.class);
		QuestionIndexProvider provider = new QuestionIndexProvider(env, instance);

		QuestionIndex build = provider.build();
		assertTrue(NullQuestionIndex.class.isInstance(build));
	}

	@Test
	public void should_build_solr_question_index() throws Exception {
		Environment env = mock(Environment.class);
		when(env.get(eq("feature.solr"), anyString())).thenReturn("true");
		Instance<SolrServer> instance = mock(Instance.class);
		Iterator<SolrServer> iterator = mock(Iterator.class);
		when(instance.iterator()).thenReturn(iterator);
		QuestionIndexProvider provider = new QuestionIndexProvider(env, instance);

		QuestionIndex build = provider.build();
		assertTrue(SolrQuestionIndex.class.isInstance(build));
	}


} 
