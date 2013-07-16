<fmt:message key="metas.home.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<fmt:message key="menu.questions" var="title"/>
<c:url value="/rss" var="rssUrl" />

<c:if test="${!currentUser.loggedIn}">
	<section class="about-banner">
		<span class="minimize-banner icon-minus"></span>
		<div class="about-content tell-me-more">
			<fmt:message key="about.home_banner.text"/>
			<a href="${linkTo[NavigationController].about}"><fmt:message key="about.home_banner.text.link"/></a>
		</div>
		<div class="about-content how-it-works">
			<h3 class="title page-title"><fmt:message key="about.home_banner.how_it_works.title"/></h3>
			<ul>
				<tags:howItWorksItem icon="icon-comment" key="about.home_banner.how_it_works.anyone_ask"/>
				<tags:howItWorksItem icon="icon-chat-empty" key="about.home_banner.how_it_works.anyone_answer"/>
				<tags:howItWorksItem icon="icon-users" key="about.home_banner.how_it_works.answers_raise_up"/>
			</ul> 
		</div>
	</section>
</c:if>


<section class="first-content">
	<div class="subheader">
		<h2 class="title page-title">
			<fmt:message key="menu.news" />
		</h2>
	</div>
	<ol class="news-list">
		<c:forEach items="${newses}" var="news">
			<li class="post-item news-item">
				<div class="post-information news-information">
					<tags:postItemInformation key="post.list.vote" count="${news.voteCount}" information="votes" htmlClass="news-info"/> 
				</div>
				<div class="summary news-summary">
					<div class="item-title-wrapper">
						<h3 class="title item-title news-title">
							<a href="/${news.id}-${news.sluggedTitle}">${news.title}</a>
						</h3>
						<time class="when" ${microdata ? 'itemprop="dateCreated"' : ''} 
							datetime="${news.createdAt}">
							<fmt:message key='touch.created'/> 
							<tags:prettyTime time="${news.createdAt}"/>
						</time>
					</div>
					<tags:lastTouchFor showTime="false" touchable="${news}"/>
				</div>
			</li>
		</c:forEach>
	</ol>
	
	<tags:questionList recentTags="${recentTags}" 
		questions="${questions}" title="${title}" rssUrl="${rssUrl}"/>
</section>
<tags:sideBar recentTags="${recentTags}" />
<tags:joyrideIntro />

