package org.mamute.search;

import java.util.Collection;
import java.util.List;

import org.mamute.model.Question;

/**
 * Created by csokol on 8/28/14.
 */
public interface QuestionIndex {
	void indexQuestion(Question q);

	@SuppressWarnings("unchecked")
	void indexQuestionBatch(Collection<Question> questions);

	List<Long> find(String query, int maxResults);

	void delete(Question question);
}
