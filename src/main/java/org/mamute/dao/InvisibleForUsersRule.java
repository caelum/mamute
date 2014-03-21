package org.mamute.dao;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.or;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.criterion.SimpleExpression;
import org.mamute.model.LoggedUser;

public class InvisibleForUsersRule implements QueryFilter{

	private LoggedUser currentUser;

	@Deprecated
	public InvisibleForUsersRule() {
	}

	@Inject
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
