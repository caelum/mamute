package br.com.caelum.brutal.infra;

import java.util.HashMap;
import java.util.Map;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@ApplicationScoped
@Component
public class ModelUrlMapping {
	Map<String, Class<?>> classForUrl = new HashMap<>();
	
	public ModelUrlMapping() {
		classForUrl.put("pergunta", Question.class);
		classForUrl.put("resposta", Answer.class);
		classForUrl.put("comentario", Comment.class);
	}
	
	public Class<?> getClassFor(String urlParam){
		Class<?> clazz = classForUrl.get(urlParam);
		if(clazz == null){
			throw new IllegalArgumentException("There is no model mapped to that urlParam: "+ urlParam);
		}
		return clazz;
	}
}
