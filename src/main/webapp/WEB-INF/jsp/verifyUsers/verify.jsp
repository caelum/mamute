<c:forEach items="${invalid}" var="u">
	delete from LoginMethod where user_id=${u.id}; delete from Users where id=${u.id};
</c:forEach>