<c:if test="${question.hasTags()}">
	<c:set var="pageTitle" 
		value="${question.mostImportantTag.name} - ${question.title}" />
</c:if>
<c:if test="${!question.hasTags()}">
	<c:set var="pageTitle" 
		value="${question.title}" />
</c:if>
<tags:header facebookMetas="${true}" 
	title="${pageTitle}" 
	description="${question.metaDescription}"/>
<div class="hidden" itemprop="breadcrumb" xmlns:v="http://rdf.data-vocabulary.org/#">
	<span typeof="v:Breadcrumb">
		<a rel="v:url" property="v:title" href="${linkTo[ListController].home(1)}">
			${t['menu.questions']}
		</a>
	</span>
	<span>»</span>
	<span typeof="v:Breadcrumb">
		<c:if test="${question.hasTags()}">
			<a rel="v:url" property="v:title" href="${linkTo[ListController].withTag(question.mostImportantTag.name,1)}">
				${question.mostImportantTag.name}
			</a>
		</c:if>
	</span>
	<span>»</span>
	<span typeof="v:Breadcrumb">
		<a rel="v:url" property="v:title" href="${linkTo[QuestionController].showQuestion(question, question.title)}">
			<c:out value="${question.title}" escapeXml="true"/>
		</a>
	</span>
</div>


<section class="first-content content">
	<c:if test="${question.hasTags()}">
		<tags:mainTags tagClass="main-tags-header" tagClassLi="main-tags-text" useSprite = "true" currentQuestion="${question}"/>
	</c:if>
	<c:if test="${markAsSolution}">
		<p class = "banner-mark-as-solution">${t['question.banner.remember']}</p>		
	</c:if>
	
	<c:if test="${showUpvoteBanner && !markAsSolution}">
		<p class = "banner-mark-as-solution">${t['question.banner.upvote']}</p>		
	</c:if>
	
	<tags:questionWith question="${question}" commentVotes="${commentsWithVotes}"/>
	<div class="subheader">
		<h2 class="title page-title">
			${question.answersCount} <tags:pluralize key="question.list.answer" count="${question.answersCount}"/>
		</h2>
	</div>
	<ul>
		<c:forEach items="${answers.votes}" var="entry" varStatus="status">
			<c:set var="answer" value="${entry.key}" />
			<c:set var="vote" value="${entry.value}" />
			<c:if test="${answer.visible || currentUser.moderator || currentUser.current.isAuthorOf(answer)}">
				<li id="answer-${answer.id}" 
					class="answer" 
					data-id="${answer.id}">
					<tags:answerWith answer="${answer}" vote="${vote}" commentVotes="${commentsWithVotes}"/>
				</li>
				<c:if test="${status.index eq 0 && shouldShowAds && question.answersCount gt 1}">
					<tags:brutal-include value="questionBottomAd" />
				</c:if>
			</c:if>
		</c:forEach>
	</ul>
	<tags:canAnswer uri="${linkTo[AnswerController].newAnswer(question)}" question="${question}"/>
	<tags:notice isLogged="${currentUser.loggedIn}" tags="${questionTags}"/>
	<c:if test="${!currentUser.loggedIn}">
		<div class="login-or-signup">
			<a class="hide-next login-form-hide">${t['auth.login_form_link']}<i class="icon-angle-right"></i></a>
			<div class="login">
				<tags:loginForm redirectUrl="${currentUrl}" />
			</div>
			<a class="hide-next signup-form-hide">${t['signup.form.submit.label']}<i class="icon-angle-right"></i></a>
			<div class="signup">
				<tags:signupForm />
			</div>
		</div>
	</c:if>
</section>
<tags:sideBar recentTags="${recentQuestionTags}" relatedQuestions="${relatedQuestions}"/>