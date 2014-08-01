<%@taglib prefix="streamer" uri="http://vraptor.org/jsp/taglib/streamer" %>

<fmt:message key="site.name" var="siteName"/>

<fmt:message key="metas.home.title" var="title"/>

<fmt:message key="metas.default.description" var="description">
	<fmt:param value="${siteName}" />
</fmt:message>

<fmt:message key="metas.generic.title" var="genericTitle" >
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<fmt:message key="menu.questions" var="title"/>

<c:if test="${!currentUser.loggedIn}">
	<section class="about-banner">
		<span class="minimize-banner icon-minus"></span>
		<div class="about-content tell-me-more">
			<h3 class="about-title title">
				<fmt:message key="about.home_banner.welcome">
					<fmt:param value="${siteName}" />
				</fmt:message>
			</h3>
			<div class="about-text">
				<fmt:message key="about.home_banner.text">
					<fmt:param value="${siteName}" />
				</fmt:message>
			</div>
			<a href="${linkTo[NavigationController].about}">
				<fmt:message key="about.home_banner.text.link">
					<fmt:param value="${siteName}" />
				</fmt:message>
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
