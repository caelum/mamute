<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@attribute name="votable" type="org.mamute.model.interfaces.Votable" required="true" %>

<c:set var="isQuestion" value="${votable['class'].name == 'org.mamute.model.Question'}" />

<h2 class="title page-title ellipsis">
	${isQuestion ? votable.title : votable.description}
</h2>
<section class="advanced-user-data user-data">
	<ul class="karma-history">
		<c:if test="${votable.votes.size() == 0 && votable.flags.size()  == 0}">
			<li class="ellipsis advanced-data-line">
				<span class="detail-info">${t['user.moderation.details.noDetails']}.</span>
			</li>
		</c:if>
		<c:forEach var="vote" items="${votable.votes}">
			<tags:detailHistoryItem vote="${vote}">
				<span class="detail-info"><fmt:formatDate value="${vote.lastUpdatedAt.toGregorianCalendar().time}" pattern="hh:mm:ss  dd/MM/yyyy"/></span>
			</tags:detailHistoryItem>
		</c:forEach>
		<c:forEach var="flag" items="${votable.flags}">
			<tags:detailHistoryItem flag="${flag}">
				<span class="detail-info ellipsis">${not empty flag.reason ? flag.reason : flag.type.toString()}</span>
			</tags:detailHistoryItem>
		</c:forEach>
	</ul>
</section>