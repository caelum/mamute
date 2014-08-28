package org.mamute.search;

import org.mamute.model.Question;
import org.mamute.model.Tag;

import java.util.Collection;
import java.util.List;

/**
 * Created by csokol on 8/28/14.
 */
public interface QuestionIndex {
	void indexQuestion(Question q);

	@SuppressWarnings("unchecked")
	void indexQuestionBatch(Collection<Question> questions);

	List<Long> findQuestionsByTitle(String title, int maxResults);

	List<Long> findQuestionsByTitleAndTag(String title, List<Tag> tags, int maxResults);
}
