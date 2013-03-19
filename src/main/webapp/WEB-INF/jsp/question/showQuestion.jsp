<tags:header plainTitle="${question.mostImportantTag} - ${question.title}" plainDescription="${question.description}"/>

<h1 class="title subheader question-title">${question.title}</h1>
<section class="first-content">
	<tags:questionWith question="${question}"/>
	<div class="subheader">
		<h2 class="title page-title">
			${question.answersCount} <fmt:message key="question.list.answer.${question.answersCount > 1 ? 'plural' : 'singular' }"/>
		</h2>
	</div>
	<ul>
		<c:forEach items="${answers.votes}" var="entry">
			<c:set var="answer" value="${entry.key}" />
			<c:set var="vote" value="${entry.value}" />
			<li id="answer-${answer.id}" class="answer ${answer.solution ? 'solution' : ''}" data-id="${answer.id}">
				<tags:answerWith answer="${answer}" vote="${vote}"/>
			</li>
		</c:forEach>
	</ul>
	<c:if test="${not empty currentUser}">
		<%@ include file="/WEB-INF/jsp/answer/answerForm.jsp"%>
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
	<h3 class="title section-title"><fmt:message key="question.tags"/></h3>
	<tags:tagsUsage tags="${questionTags}"/>
</aside>