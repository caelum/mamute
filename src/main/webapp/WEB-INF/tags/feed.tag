<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="rssFeed" type="org.mamute.infra.rss.read.RSSFeed" required="true" %>
<%@attribute name="rssUrl" type="java.lang.String" required="true" %>
<%@attribute name="rssType" type="java.lang.String" required="true" %>
<c:if test="${not empty rssFeed.channel.items}">
	<div class="subheader">
		<h2 class="title page-title">
			${t['${rssType}.rss.title']}
		</h2>
		<a href="${rssUrl}" class="rss-link"><i class="icon-rss"></i></a>
	</div>
	<ol class="sidebar-list">
		<c:forEach items="${rssFeed.channel.items}" var="item">
			<li class="post-item sidebar-item">
				<div class="summary sidebar-summary">
					<time class="when" datetime="${item.pubDate}">
						<fmt:formatDate value="${item.pubDate.toDate()}" pattern="dd/MM/yyyy"/>
					</time> -
					<h3 class="title item-title sidebar-item-title">
						<a href="${item.link}"><c:out escapeXml="${true}" value="${item.title}"/></a>
					</h3>
				</div>
			</li>
		</c:forEach>
	</ol>
	<a class="more-items" href="${t['${rssType}.rss.url']}">${t['${rssType}.rss.more']}</a>
	
</c:if>
