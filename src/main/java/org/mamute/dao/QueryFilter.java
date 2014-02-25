package org.mamute.dao;

import org.hibernate.Criteria;

public interface QueryFilter {
	public Criteria addFilter(String modelAlias, Criteria criteria);
}
