<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="title" type="java.lang.String" required="true"%>
<%@attribute name="recentTags" type="java.util.List" required="true"%>
<%@attribute name="questions" type="java.util.List" required="true"%>
<%@attribute name="rssUrl" type="java.lang.String" required="false"%>
<%@attribute name="tag" type="br.com.caelum.brutal.model.Tag" required="false"%>

<section class="first-content">
	<div class="subheader">
		<h2 class="title page-title">
			${title}
			<c:if test="${not empty tag}">
				: <tags:tag tag="${tag}"/>
				<a href="<c:url value='/ranking/${tag.name}'/>" title='<fmt:message key="users.ranking.tag.title"/> ${tag.name}' class="icon-trophy"></a>
			</c:if>
		</h2>
		<tags:brutal-include value="mainTags"/>
		<c:if test="${not empty rssUrl}">
			<a href="${rssUrl}" class="rss-link"><i class="icon-rss"></i></a>
		</c:if>
	</div>
	<c:if test="${not empty questions}">
		<ol class="question-list">
			<c:forEach var="question" items="${questions }">
				<tags:questionListItem question="${question}"/>
			</c:forEach>
		</ol>
	</c:if>
	<c:if test="${empty questions}">
		<h2 class="title section-title"><fmt:message key="questions.empty_list" /></h2>
	</c:if>
	<tags:pagination url="${currentUrl}" currentPage="${currentPage}" totalPages="${totalPages}" delta="2"/>
</section>
<aside class="sidebar">
	<div class="subheader">
		<h3 class="title page-title"><fmt:message key="tags.main"/></h3>
	</div>
	<tags:brutal-include value="mainTags"/>
	<div class="subheader">
		<h3 class="title page-title"><fmt:message key="tags.recent"/></h3>
	</div>
	<tags:recentTagsUsage tagsUsage="${recentTags}"/>
</aside>
<ol id="intro">
	<tags:joyrideTip className="votes" options="tipLocation:top" key="intro.home.votes" />
	<tags:joyrideTip className="answers" options="tipLocation:top" key="intro.home.answers" />
	<tags:joyrideTip className="main-tags" options="tipLocation:left" key="intro.home.tags" />
	<tags:joyrideTip className="about" options="tipLocation:bottom" key="intro.about" />
</ol>