<fmt:message key="metas.home.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<section class="first-content">
	<div class="subheader">
		<h2 class="title page-title replace"></h2>
	</div>
	<c:if test="${not empty questions}">
		<ol class="question-list">
			<c:forEach var="question" items="${questions }">
				<tags:list-question-item question="${question}"/>
			</c:forEach>
		</ol>
	</c:if>
	<c:if test="${empty questions}">
		<h2 class="title section-title"><fmt:message key="questions.empty_list" /></h2>
	</c:if>
</section>
<aside class="sidebar">
	<div class="subheader">
		<h3 class="title page-title">Tags Principais</h3>
	</div>
	<ol class="main-tags tags-usage">
		<%@include file="../mainTags.jsp" %>
	</ol>
	<div class="subheader">
		<h3 class="title page-title"><fmt:message key="tags.recent"/></h3>
	</div>
	<tags:recentTagsUsage tagsUsage="${recentTags}"/>
</aside>
