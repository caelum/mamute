<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="description" value="${t['metas.default.description'].args(siteName)}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

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
					    href="${linkTo[TagPageController].editTagPageForm(tagPage.tagUriName)}">
						${t['edit']}
					</a>
				</li>
			</ul>
		</div>
	</c:if>
</section>
<tags:sideBar recentTags="${recentQuestionTags}" relatedQuestions="${relatedQuestions}"/>
