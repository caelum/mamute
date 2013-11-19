<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@attribute name="votable" type="br.com.caelum.brutal.model.interfaces.Votable" required="true" %>

<c:set var="isQuestion" value="${votable.class.name == 'br.com.caelum.brutal.model.Question'}" />

<h2 class="title page-title">
	<span class="grey-text">
		<c:if test="${isQuestion}">
			${votable.title}
		</c:if>
		<c:if test="${! isQuestion}">
			Answer Id: ${votable.id}
		</c:if>
	</span>
</h2>
<section class="advanced-user-data user-data">
	<ul class="karma-history">
		<c:forEach var="vote" items="${votable.votes}">
			<tags:voteHistoryItem vote="${vote}">
				<span class="vote-time"><fmt:formatDate value="${vote.lastUpdatedAt.toGregorianCalendar().time}" pattern="hh:mm:ss  dd/MM/yyyy"/></span>
			</tags:voteHistoryItem>
		</c:forEach>
	</ul>
</section>