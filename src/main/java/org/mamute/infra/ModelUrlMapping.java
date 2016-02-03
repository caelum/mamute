package org.mamute.infra;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.mamute.factory.MessageFactory;
import org.mamute.model.Answer;
import org.mamute.model.Comment;
import org.mamute.model.News;
import org.mamute.model.Question;

@ApplicationScoped
public class ModelUrlMapping {

    private Map<String, Class<?>> classForUrl = new HashMap<>();

    @Deprecated
    public ModelUrlMapping() { }

    @Inject
    public ModelUrlMapping(MessageFactory messageFactory) {
		classForUrl.put(messageFactory.build("question", "question.type_name").getMessage(), Question.class);
		classForUrl.put(Question.class.getSimpleName(), Question.class);
		classForUrl.put(messageFactory.build("answer", "answer.type_name").getMessage(), Answer.class);
		classForUrl.put(Answer.class.getSimpleName(), Answer.class);
		classForUrl.put(messageFactory.build("comment", "comment.type_name").getMessage(), Comment.class);
		classForUrl.put(Comment.class.getSimpleName(), Comment.class);
		classForUrl.put(messageFactory.build("news", "news.type_name").getMessage(), News.class);
		classForUrl.put(News.class.getSimpleName(), News.class);
	}
	
	public Class<?> getClassFor(String urlParam){
		Class<?> clazz = classForUrl.get(urlParam);
		if (clazz == null) {
			throw new NotFoundException("There is no model mapped to that urlParam: "+ urlParam);
		}
		return clazz;
	}
}
