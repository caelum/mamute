<fmt:message key="metas.about.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<div class="about-section sub-header">
	<h2 class="title about-title"><fmt:message key="about.title.welcome" /></h2>
	<p class="about-main"></p>
</div>

<div class="about-section">
	<h2 class="title about-title"><fmt:message key="about.tips"/></h2>
	<div class="about-text">
		<ul class="about-tips">
			<li><fmt:message key="about.tips.solved"/></li>
			<li><fmt:message key="about.tips.avoid.thanks"/></li>
			<li><fmt:message key="about.tips.kkk"/></li>
			<li><fmt:message key="about.tips.greetings"/></li>
		</ul>
	</div>
</div>

<div class="about-section">
	<h2 class="title about-title"><fmt:message key="about.reputation.permission" /></h2>
	<div class="about-text">
		<ul class="rules-list">
			<li>
				<fmt:message key="about.reputation.upvote" >
					<fmt:param value="${VOTE_UP}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.flag">
					<fmt:param value="${CREATE_FLAG}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.edit.question">
					<fmt:param value="${EDIT_QUESTION}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.edit.answer">
					<fmt:param value="${EDIT_ANSWER}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.downvote">
					<fmt:param value="${VOTE_DOWN}"/>		
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.moderate">
					<fmt:param value="${MODERATE_EDITS}"/>
				</fmt:message>
			</li>
		</ul>
	</div><!-- about-text -->
</div><!-- about-section -->

<div class="about-section">
	<h2 class="title about-title"><fmt:message key="about.reputation.gain" /></h2>
	<div class="about-text">
		<ul class="rules-list">
			<li>
				<fmt:message key="about.reputation.gain.question.answered">
					<fmt:param value="${ANSWERED_QUESTION}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.question.asked">
					<fmt:param value="${ASKED_QUESTION}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.question.solution" >
					<fmt:param value="${SOLVED_QUESTION_AUTHOR}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.answer.solution">
					<fmt:param value="${SOLUTION_AUTHOR}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.answer.voted_up">
					<fmt:param value="${ANSWER_VOTED_UP}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.question_and_answer.voted_down">
					<fmt:param value="${ANSWER_VOTED_DOWN}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.question.voted_up">
					<fmt:param value="${QUESTION_VOTED_UP}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.approved_edit">
					<fmt:param value="${APPROVED_INFORMATION}"/>		
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.comment.voted_up">
					<fmt:param value="${COMMENT_VOTED_UP}"/>
				</fmt:message>
			</li>
		</ul>
	</div><!-- about-text -->
</div><!-- about-section -->

<div class="about-section">
	<h2 class="title about-title" id="faq"><fmt:message key="about.reputation.faq" /></h2>
	<div class="about-text">
		<ul class="about-faq">
			<li>
				<p class="about-question"><fmt:message key="about.faq.what_to_ask.question"/></p>
				<p class="about-answer"><fmt:message key="about.faq.what_to_ask.answer"/></p>
			</li>
			<li>
				<p class="about-question"><fmt:message key="about.faq.what_not_to_ask.question"/></p>
				<p class="about-answer"><fmt:message key="about.faq.what_not_to_ask.answer"/></p>
			</li>
			<li>
				<p class="about-question"><fmt:message key="about.faq.first_page_questions.question"/></p>
				<p class="about-answer"><fmt:message key="about.faq.first_page_questions.answer"/></p>
			</li>
			<li>
				<p class="about-question"><fmt:message key="about.faq.edition.question"/></p>
				<p class="about-answer"><fmt:message key="about.faq.edition.answer"/></p>
			</li>
			<li>
				<p class="about-question"><fmt:message key="about.faq.bad_question.question"/></p>
				<p class="about-answer"><fmt:message key="about.faq.bad_question.answer"/></p>
			</li>
		</ul>
	</div>
</div>