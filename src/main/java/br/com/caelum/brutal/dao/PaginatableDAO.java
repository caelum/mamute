package br.com.caelum.brutal.dao;

import java.util.List;

import br.com.caelum.brutal.dao.WithUserDAO.OrderType;
import br.com.caelum.brutal.model.User;

public interface PaginatableDAO {

	List<?> postsToPaginateBy(User author, OrderType order, Integer page);
	
	Long countWithAuthor(User author);

	Long numberOfPagesTo(User author);

}
