<fmt:message key="metas.home.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<section class="news-content content">
	<div class="subheader news-aside-list-header">
		<h2 class="title page-title"><fmt:message key="menu.news"/>	</h2>	
		<a href="/noticias/rss" class="rss-link"><i class="icon-rss "></i></a>
		<a href="<c:url value="/nova-noticia"/>" class="send-news">
			<fmt:message key="menu.new_news" />
		</a>				
	</div>
	<c:if test="${not empty newses}">
		<ol class="news-list">
			<c:forEach var="news" items="${newses}">
				<tags:newsListItem news="${news}"/>
			</c:forEach>
		</ol>
	</c:if>
	<c:if test="${empty newses}">
		<h2 class="title section-title"><fmt:message key="newses.empty_list" /></h2>
	</c:if>
	<tags:pagination url="${currentUrl}" currentPage="${currentPage}" totalPages="${totalPages}" delta="2"/>
</section>
