package org.mamute.brutauth.auth.rules;

import static org.mamute.auth.rules.PermissionRulesConstants.INACTIVE_QUESTION;
import static org.mamute.auth.rules.Rules.hasKarma;

import javax.inject.Inject;

import org.mamute.model.LoggedUser;
import org.mamute.model.Question;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class InactiveQuestionRequiresMoreKarmaRule implements CustomBrutauthRule{

	@Inject public LoggedUser user;
	
	public boolean isAllowed(Question question){
		if(question.isInactiveForOneMonth()) return hasKarma(INACTIVE_QUESTION).isAllowed(user.getCurrent(), question);
		return true;
	}
}
