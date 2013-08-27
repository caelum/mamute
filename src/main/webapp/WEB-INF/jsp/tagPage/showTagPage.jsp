<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${tagPage.tagName}" description="${description}"/>

<section class="first-content content">
	<tags:rssTagHeader tag="${tagPage.tag}"/>
	
	<tags:tagTabs tag="${tagPage.tag}" hasAbout="${hasAbout}"/>

	<div class="post-container about-tag">
		<div itemprop="articleBody" class="post-text">
			${tagPage.markedAbout}
		</div>
	</div>
	<c:if test="${currentUser.moderator}">
		<div class="post-interactions">
			<ul class="post-action-nav piped-nav nav">
				<li class="nav-item">
					<a class="post-action requires-login"
					    href="${linkTo[TagPageController].editTagPageForm[tagPage.tagUriName]}">
						<fmt:message key="edit" />
					</a>
				</li>
			</ul>
		</div>
	</c:if>
</section>
<tags:sideBar recentTags="${recentQuestionTags}" relatedQuestions="${relatedQuestions}"/>
