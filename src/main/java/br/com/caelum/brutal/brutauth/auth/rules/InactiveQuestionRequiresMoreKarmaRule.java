package br.com.caelum.brutal.brutauth.auth.rules;

import static br.com.caelum.brutal.auth.rules.PermissionRulesConstants.INACTIVE_QUESTION;
import static br.com.caelum.brutal.auth.rules.Rules.hasKarma;

import javax.inject.Inject;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class InactiveQuestionRequiresMoreKarmaRule implements CustomBrutauthRule{

	@Inject public LoggedUser user;
	
	public boolean isAllowed(Question question){
		if(question.isInactiveForOneMonth()) return hasKarma(INACTIVE_QUESTION).isAllowed(user.getCurrent(), question);
		return true;
	}
}
