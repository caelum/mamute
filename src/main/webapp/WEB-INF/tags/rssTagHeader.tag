<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute type="br.com.caelum.brutal.model.Tag" name="tag" required="false"%>
<%@attribute type="java.lang.String" name="rssUrl" required="false"%>
<%@attribute type="java.lang.String" name="title" required="false"%>

<div class="subheader ${not empty tag? 'subheader-with-tag' : 'subheader-without-tag' }">
	<c:if test="${not empty title}">
		<h2 class="title page-title">
			${title}
		</h2>
	</c:if>
		<tags:brutal-include value="headerTags"/>
		<c:if test="${not empty rssUrl}">
			<a href="${rssUrl}" class="rss-link"><i class="icon-rss"></i></a>
		</c:if>
</div>