package br.com.caelum.brutal.dao;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.or;

import org.hibernate.Criteria;
import org.hibernate.criterion.SimpleExpression;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class InvisibleForUsersRule implements QueryFilter{

	private final LoggedUser currentUser;

	public InvisibleForUsersRule(LoggedUser currentUser) {
		this.currentUser = currentUser;
	}
	
	public Criteria addFilter(String modelAlias, Criteria criteria) {
		if(currentUser.isModerator()) return criteria;
		if(!currentUser.isLoggedIn()) return criteria.add(isVisible(modelAlias));
		return criteria.add(or(isVisible(modelAlias), isAuthor(modelAlias)));
	}

	private SimpleExpression isAuthor(String modelAlias) {
		return eq(modelAlias+".author.id", currentUser.getCurrent().getId());
	}

	private SimpleExpression isVisible(String modelAlias) {
		return eq(modelAlias+".moderationOptions.invisible", false);
	}


}
