package br.com.caelum.brutal.dao;

import org.hibernate.Criteria;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ModeratorOrVisibleNewsFilter implements QueryFilter{

	private final LoggedUser currentUser;
	private final VisibleNewsFilter visibleNewsFilter;

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
