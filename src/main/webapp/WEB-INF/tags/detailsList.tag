<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@attribute name="votable" type="br.com.caelum.brutal.model.interfaces.Votable" required="true" %>

<c:set var="isQuestion" value="${votable.class.name == 'br.com.caelum.brutal.model.Question'}" />

<section>
	<div class="arrow up-arrow"></div>
	<c:if test="${isQuestion}">
		<h2 class="title page-title">${votable.title}</h2>
	</c:if>
	<c:if test="${! isQuestion}">
		<h3 class="title page-title">Answer Id: ${votable.id}</h3>
	</c:if>
	<span class="post-text question-description"><fmt:message key="user.moderation.details.author"/>: ${votable.author.name}</span>
	<p>${votable.description}</p>
	<ul class="details-table">
		<c:if test="${votable.votes.size() > 0}">
			<li class="details-table-row details-table-header">
				<div class="details-table-cell">
					<fmt:message key="user.moderation.details.type"/>
				</div>
				<div class="details-table-author details-table-cell">
					<fmt:message key="user.moderation.details.author"/>
				</div>
				<div class="details-table-date details-table-cell">
					<fmt:message key="user.moderation.details.date"/>
				</div>
			</li>
		</c:if>
		<c:forEach items="${votable.votes}" var="vote">
			<li class="reset details-table-row details-table-${vote.type == 'UP' ? 'vote-up' : 'vote-down'}">
				<div class="arrow ${vote.type == 'UP' ? 'up' : 'down'}-arrow">
					${vote.type}
				</div>
				<div class="details-table-author details-table-cell">
					<a href="${linkTo[UserProfileController].showProfile(vote.author, vote.author.sluggedName)}">${vote.author.name}</a>
				</div>
				<div class="details-table-date details-table-cell">
					${vote.lastUpdatedAt}
				</div>
			</li>
		</c:forEach>
	</ul>
	<%-- <table class="details">
		<c:if test="${votable.votes.size() > 0}">
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
		</c:if>
		<c:forEach items="${votable.votes}" var="vote">
			<tr class="${vote.type == 'UP' ? 'vote-up' : 'vote-down'}">
				<td>
					${vote.type}
				</td>
				<td>
					<a href="${linkTo[UserProfileController].showProfile(vote.author, vote.author.sluggedName)}">${vote.author.name}</a>
				</td>
				<td>
					${vote.lastUpdatedAt}
				</td>
			</tr>
		</c:forEach>
	</table> --%>
</section>