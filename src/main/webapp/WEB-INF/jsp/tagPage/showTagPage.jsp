<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${tagPage.tagName}" description="${description}"/>

<section class="first-content content">
	<h1 itemprop="name" class="title subheader main-thread-title"><fmt:message key="tag_page.title"/> ${tagPage.tagName}</h1>
	<div class="post-container">
		<div itemprop="articleBody" class="post-text">
			${tagPage.markedAbout}
		</div>
	</div>
</section>
<tags:sideBar recentTags="${recentQuestionTags}" relatedQuestions="${relatedQuestions}"/>
