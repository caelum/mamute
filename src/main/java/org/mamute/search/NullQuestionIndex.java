package org.mamute.search;

import org.mamute.model.Question;
import org.mamute.model.Tag;

import javax.enterprise.inject.Vetoed;
import java.util.Collection;
import java.util.List;

@Vetoed
public class NullQuestionIndex implements QuestionIndex {
	@Override
	public void indexQuestion(Question q) {
		//does nothing
	}

	@Override
	public void indexQuestionBatch(Collection<Question> questions) {
		//does nothing
	}

	@Override
	public List<Long> findQuestionsByTitle(String title, int maxResults) {
		//does nothing
		return null;
	}

	@Override
	public List<Long> findQuestionsByTitleAndTag(String title, List<Tag> tags, int maxResults) {
		//does nothing
		return null;
	}
}
