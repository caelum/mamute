<%@ tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form class="search-form" action="${linkTo[SearchController].search[null]}" method="get">
	<input class="text-input" name="query" placeholder="<fmt:message key="search.placeholder"/>" type="text" />
	<input type="submit" value="go" />
</form>