package org.mamute.dao;

import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.SimpleExpression;

public class VisibleNewsFilter implements QueryFilter{

	@Override
	public Criteria addFilter(String modelAlias, Criteria criteria) {
		return criteria.add(and(
				moderationOptionsVisible(modelAlias),
				approved(modelAlias))
		);
	}

	private Criterion approved(String modelAlias) {
		return eq(modelAlias+".approved", true);
	}

	private SimpleExpression moderationOptionsVisible(String modelAlias) {
		return eq(modelAlias+".moderationOptions.invisible", false);
	}

}
