<h2 class="title question-title">${question.title}</h2>
<section class="first-content">
	<tags:questionWith question="${question}"/>
	<h2 class="title page-title">
		${question.answersCount} <fmt:message key="question.list.answer.${question.answersCount > 1 ? 'plural' : 'singular' }"/>
	</h2>
	<ul>
		<c:forEach items="${answers.votes}" var="entry">
			<c:set var="answer" value="${entry.key}" />
			<c:set var="vote" value="${entry.value}" />
			<li class="answer ${answer.solution? 'solution' : ''}" data-id="${answer.id}">
				<tags:answerWith answer="${answer}" vote="${vote}"/>
			</li>
		</c:forEach>
	</ul>
	<c:if test="${not empty currentUser}">
		<%@ include file="/WEB-INF/jsp/answer/answerForm.jsp"%>
	</c:if>
	<c:if test="${empty currentUser}">
		<div class="login-or-signup">
			<a class="login" href="${linkTo[AuthController].loginForm}">
				<fmt:message key="auth.login_form_link"/>
			</a>
			<div class="signup">
				<tags:signupForm />
			</div>
		</div>
	</c:if>
</section>
<aside class="sidebar">
	<h3 class="title section-title"><fmt:message key="question.tags"/></h3>
	<tags:tagsFor question="${question}" />
</aside>