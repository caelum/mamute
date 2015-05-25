package org.mamute.brutauth.auth.rules;

import static org.mamute.auth.rules.Rules.hasKarma;

import javax.inject.Inject;

import org.mamute.auth.rules.PermissionRules;
import org.mamute.dao.QuestionDAO;
import org.mamute.infra.ModelUrlMapping;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class InactiveQuestionRequiresMoreKarmaRule implements CustomBrutauthRule{

	@Inject private LoggedUser user;
	@Inject private ModelUrlMapping urlMapping;
	@Inject private QuestionDAO questions;
	@Inject private EnvironmentKarma environmentKarma;
	
	public boolean isAllowed(Question question, String onWhat, Long id){
		if(question == null) {
			Class<?> interactedType = urlMapping.getClassFor(onWhat);
			question  = interactedType.isAssignableFrom(Question.class) ? questions.getById(id) : null;
		}
		
		if(question != null && question.isInactiveForOneMonth()) {
			long karmaRequired = environmentKarma.get(PermissionRules.INACTIVATE_QUESTION);
			return hasKarma(karmaRequired).isAllowed(user.getCurrent(), question);
		}
		return true;
	}
}
