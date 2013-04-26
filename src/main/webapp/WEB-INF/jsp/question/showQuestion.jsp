<tags:header facebookMetas="${true}" title="${question.mostImportantTag} - ${question.title}" />
<section class="first-content">
	<tags:questionWith question="${question}" commentVotes="${commentsWithVotes}"/>
	<div class="subheader">
		<h2 class="title page-title">
			${question.answersCount} <fmt:message key="question.list.answer.${question.answersCount > 1 ? 'plural' : 'singular' }"/>
		</h2>
	</div>
	<ul>
		<c:forEach items="${answers.votes}" var="entry">
			<c:set var="answer" value="${entry.key}" />
			<c:set var="vote" value="${entry.value}" />
			<c:if test="${answer.visible || currentUser.moderator || currentUser.isAuthorOf(answer)}">
				<li id="answer-${answer.id}" 
					class="answer" 
					data-id="${answer.id}">
					<tags:answerWith answer="${answer}" vote="${vote}" commentVotes="${commentsWithVotes}"/>
				</li>
			</c:if>
		</c:forEach>
	</ul>
	<c:if test="${not empty currentUser && !question.alreadyAnsweredBy(currentUser)}">
		<tags:answerForm uri="${linkTo[AnswerController].newAnswer[question]}" />
	</c:if>
	<c:if test="${not empty currentUser && question.alreadyAnsweredBy(currentUser)}">
		<div class="message alert already-answered">
			<fmt:message key="answer.errors.already_answered"/>
		</div>
	</c:if>
	<c:if test="${empty currentUser}">
		<div class="login-or-signup">
			<div class="login">
				<tags:loginForm redirectUrl="${currentUrl}" />
			</div>
			<div class="signup">
				<tags:signupForm />
			</div>
		</div>
	</c:if>
</section>
<aside class="sidebar">
	<h3 class="title page-title"><fmt:message key="question.tags"/></h3>
	<tags:tagsUsage tags="${questionTags}"/>
</aside>