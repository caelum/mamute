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
			<h3 class="about-title title"><fmt:message key="about.home_banner.welcome"/></h3>
			<div class="about-text">
				<fmt:message key="about.home_banner.text"/>
			</div>
			<a href="${linkTo[NavigationController].about}"><fmt:message key="about.home_banner.text.link"/></a>
		</div>
		<div class="about-content how-it-works">
			<ul>
				<tags:howItWorksItem icon="icon-comment" key="about.home_banner.how_it_works.anyone_ask"/>
				<tags:howItWorksItem icon="icon-chat-empty" key="about.home_banner.how_it_works.anyone_answer"/>
				<tags:howItWorksItem icon="icon-users" key="about.home_banner.how_it_works.answers_raise_up"/>
			</ul> 
		</div>
	</section>
</c:if>


<section class="first-content content">
	<tags:questionList recentTags="${recentTags}" 
		questions="${questions}" title="${title}" rssUrl="${rssUrl}"/>
</section>
<tags:joyrideIntro />

<tags:sideBar recentTags="${recentTags}" />