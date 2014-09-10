<%@taglib prefix="streamer" uri="http://vraptor.org/jsp/taglib/streamer" %>

<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="title" value="${t['metas.home.title']}"/>

<c:set var="description" value="${t['metas.default.description'].args(siteName)}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<c:set var="title" value="${t['menu.questions']}"/>

<c:if test="${!currentUser.loggedIn}">
	<section class="about-banner">
		<span class="minimize-banner icon-minus"></span>
		<div class="about-content tell-me-more">
			<h3 class="about-title title">
				${t['about.home_banner.welcome'].args(siteName)}
			</h3>
			<div class="about-text">
				${t['about.home_banner.text'].args(siteName)}
			</div>
			<a href="${linkTo[NavigationController].about}">
				${t['about.home_banner.text.link'].args(siteName)}
			</a>
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


<div id="question-list-container"></div>
<div id="side-bar-container"></div>
<streamer:stream>
    <streamer:page url="questionListPagelet"/>
    <streamer:page url="sideBarPagelet"/>
</streamer:stream>

<tags:joyrideIntro />
