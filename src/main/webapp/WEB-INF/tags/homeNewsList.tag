<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="newses" type="java.util.List" required="true"%>

<div class="subheader news-aside-list-header">
	<h2 class="title page-title">
		<fmt:message key="menu.news" />
	</h2>
	<a href="/noticias/rss" class="rss-link"><i class="icon-rss"></i></a>
</div>
	<ol class="news-list">
		<c:forEach items="${newses}" var="news">
			<li class="post-item news-item">
				<div class="summary news-summary">
					<time class="when" ${microdata ? 'itemprop="dateCreated"' : ''} 
						datetime="${news.createdAt}">
						<fmt:formatDate value="${news.createdAt.toGregorianCalendar().time}" pattern="dd/MM/yyyy"/>
					</time> -
					<h3 class="title item-title home-news-title">
						<a href="/noticias/${news.id}-${news.sluggedTitle}">${news.title}</a>
					</h3>
				</div>
			</li>
		</c:forEach>
	</ol>
	<a class="other-news" href="<c:url value='/noticias'/>"><fmt:message key="news.link" /></a>
	