package org.mamute.infra;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.mamute.model.Answer;
import org.mamute.model.Comment;
import org.mamute.model.News;
import org.mamute.model.Question;

@ApplicationScoped
public class ModelUrlMapping {
	private Map<String, Class<?>> classForUrl = new HashMap<>();
	
	public ModelUrlMapping() {
		classForUrl.put("pergunta", Question.class);
		classForUrl.put("resposta", Answer.class);
		classForUrl.put("comentario", Comment.class);
		classForUrl.put("noticia", News.class);
	}
	
	public Class<?> getClassFor(String urlParam){
		Class<?> clazz = classForUrl.get(urlParam);
		if (clazz == null) {
			throw new NotFoundException("There is no model mapped to that urlParam: "+ urlParam);
		}
		return clazz;
	}
}
