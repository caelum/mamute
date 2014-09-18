<%@ tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${env.supports('feature.solr') || env.supports('feature.google_search')}">

	<c:if test="${env.supports('feature.solr')}">
		<c:set var="linkToSearch" value="${linkTo[SolrSearchController].search()}"/>
	</c:if>
	<c:if test="${env.supports('feature.google_search')}">
		<c:set var="linkToSearch" value="${linkTo[GoogleSearchController].search()}"/>
	</c:if>
	
	<form class="search-form" action="${linkToSearch}" method="get">
		<input class="text-input" name="query" placeholder="${t['search.placeholder']}" type="text" />
		<input type="submit" value="go" />
	</form>
</c:if>