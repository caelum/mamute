<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="rssFeed" type="br.com.caelum.brutal.infra.rss.read.RSSFeed" required="true" %>

<c:if test="${not empty rssFeed.channel.items}">
	<h2 class="title page-title subheader">Ofertas de Empregos</h2>
	
	<ol>
		<c:forEach items="${rssFeed.channel.items}" var="item">
			<li>
	      		<span><a href="${item.link}" target="_blank"><c:out escapeXml="${true}" value="${item.title}"/></a></span>
	      		<span class="data"> - <tags:prettyTime time="${item.pubDate}"/></span>
			</li>
		</c:forEach>
	</ol>
</c:if>
