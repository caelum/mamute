<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@attribute name="votable" type="br.com.caelum.brutal.model.interfaces.Votable" required="true" %>

<c:set var="isQuestion" value="${votable.class.name == 'br.com.caelum.brutal.model.Question'}" />

<section>
	<c:if test="${isQuestion}">
		<h2 class="title page-title">${votable.title}</h2>
	</c:if>
	<c:if test="${! isQuestion}">
		<h3 class="title page-title">Answer Id: ${votable.id}</h3>
	</c:if>
	<span class="post-text question-description"><fmt:message key="user.moderation.details.author"/>: ${votable.author.name}</span>
	<table class="details">
		<tr>
			<th>
				<fmt:message key="user.moderation.details.type"/>
			</th>
			<th>
				<fmt:message key="user.moderation.details.author"/>
			</th>
			<th>
				<fmt:message key="user.moderation.details.date"/>
			</th>
		</tr>
	
		<c:forEach items="${votable.votes}" var="vote">
			<tr>
				<td>
					${vote.type}
				</td>
				<td>
					${vote.author.name}
				</td>
				<td>
					${vote.lastUpdatedAt}
				</td>
			</tr>
		</c:forEach>
	</table>
</section>