package br.com.caelum.brutal.dao;

import javax.inject.Inject;

import org.hibernate.Criteria;

import br.com.caelum.brutal.model.LoggedUser;

public class ModeratorOrVisibleNewsFilter implements QueryFilter{

	private LoggedUser currentUser;
	private VisibleNewsFilter visibleNewsFilter;

	@Deprecated
	public ModeratorOrVisibleNewsFilter() {
	}

	@Inject
	public ModeratorOrVisibleNewsFilter(LoggedUser currentUser, VisibleNewsFilter visibleNewsFilter) {
		this.currentUser = currentUser;
		this.visibleNewsFilter = visibleNewsFilter;
	}
	
	@Override
	public Criteria addFilter(String modelAlias, Criteria criteria) {
		if(currentUser.isModerator()) return criteria;
		return visibleNewsFilter.addFilter(modelAlias, criteria);
	}

}
