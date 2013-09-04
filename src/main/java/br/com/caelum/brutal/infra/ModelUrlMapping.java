package br.com.caelum.brutal.infra;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.Question;

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
