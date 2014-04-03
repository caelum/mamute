<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="newses" type="java.util.List" required="true"%>

<div class="subheader">
	<h2 class="title page-title">
		<fmt:message key="menu.news" />
	</h2>
	<a href="${linkTo[RssController].newsRss}" class="rss-link"><i class="icon-rss"></i></a>
</div>
<ol class="sidebar-list">
	<c:forEach items="${newses}" var="news">
		<li class="post-item sidebar-item">
			<div class="summary sidebar-summary">
				<time class="when" ${microdata ? 'itemprop="dateCreated"' : ''} 
					datetime="${news.createdAt}">
					<fmt:formatDate value="${news.createdAt.toGregorianCalendar().time}" pattern="dd/MM/yyyy"/>
				</time> -
				<h3 class="title item-title sidebar-item-title">
					<a href="${linkTo[NewsController].showNews(news, news.sluggedTitle)}">${news.title}</a>
				</h3>
			</div>
		</li>
	</c:forEach>
</ol>
<a class="more-items" href="${linkTo[ListController].news}"><fmt:message key="news.link" /></a>
	