<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="description" value="${t['metas.default.description'].args(siteName)}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<section class="news-content content">
	<div class="subheader news-aside-list-header">
		<h2 class="title page-title">${t['menu.news']}	</h2>	
		<a href="${linkTo[RssController].newsRss}" class="rss-link"><i class="icon-rss "></i></a>
		<a href="${linkTo[NewsController].newsForm}" class="send-news">
			${t['menu.new_news']}
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
		<h2 class="title section-title">${t['newses.empty_list']}</h2>
	</c:if>
	<tags:pagination url="${currentUrl}" currentPage="${currentPage}" totalPages="${totalPages}" delta="2"/>
</section>
