package org.mamute.dao;

import java.util.List;

import org.mamute.dao.WithUserPaginatedDAO.OrderType;
import org.mamute.model.User;

public interface PaginatableDAO {

	List<?> ofUserPaginatedBy(User author, OrderType order, Integer page);
	
	Long countWithAuthor(User author);

	Long numberOfPagesTo(User author);

}
