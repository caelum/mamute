<tags:header facebookMetas="${true}" 
	title="${question.mostImportantTag.name} - ${question.title}" 
	description="${question.metaDescription}"/>
<div class="breadcrumb" itemprop="breadcrumb" xmlns:v="http://rdf.data-vocabulary.org/#">
	<span typeof="v:Breadcrumb">
		<a rel="v:url" property="v:title" href="${linkTo[ListController].home[1]}">
			<fmt:message key="menu.questions"/>
		</a>
	</span>
	<span>»</span>
	<span typeof="v:Breadcrumb">
		<a rel="v:url" property="v:title" href="${linkTo[ListController].withTag[question.mostImportantTag.name][1]}">
			${question.mostImportantTag.name}
		</a>
	</span>
	<span>»</span>
	<span typeof="v:Breadcrumb">
		<a rel="v:url" property="v:title" href="${linkTo[QuestionController].showQuestion[question][question.title]}">
			${question.title}
		</a>
	</span>
</div>
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
			<c:if test="${answer.visible || currentUser.moderator || currentUser.current.isAuthorOf(answer)}">
				<li id="answer-${answer.id}" 
					class="answer" 
					data-id="${answer.id}">
					<tags:answerWith answer="${answer}" vote="${vote}" commentVotes="${commentsWithVotes}"/>
				</li>
			</c:if>
		</c:forEach>
	</ul>
</section>
<tags:sideBar recentTags="${recentTags}" />

<tags:canAnswer uri="/responder/${question.id}" question="${question}"/>
<tags:notice isLogged="${currentUser.loggedIn}" tags="${questionTags}"/>
<c:if test="${!currentUser.loggedIn}">
	<div class="login-or-signup">
		<a class="hide-next login-form-hide"><fmt:message key="auth.login_form_link"/><i class="icon-angle-right"></i></a>
		<div class="login">
			<tags:loginForm redirectUrl="${currentUrl}" />
		</div>
		<a class="hide-next signup-form-hide"><fmt:message key="signup.form.submit.label"/><i class="icon-angle-right"></i></a>
		<div class="signup">
			<tags:signupForm />
		</div>
	</div>
</c:if>