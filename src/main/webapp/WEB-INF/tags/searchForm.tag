<%@ tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${env.supports('feature.solr') || env.supports('feature.google_search')}">
	<form class="search-form" action="/search" method="get">
		<input class="text-input" name="query" placeholder="<fmt:message key="search.placeholder"/>" type="text" />
		<input type="submit" value="go" />
	</form>
</c:if>