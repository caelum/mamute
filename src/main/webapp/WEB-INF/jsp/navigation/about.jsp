<fmt:message key="metas.about.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<div class="about-section sub-header">
	<div class="about-section about-info">
		<div class="big-text">
			<fmt:message key="about.info">
				<fmt:param value="http://www.caelum.com.br/cursos-java/"/>
				<fmt:param value="http://www.guj.com.br/"/>
				<fmt:param value="http://www.guj.com.br/perguntas"/>
			</fmt:message>
		</div>
	</div>
</div>

<div class="about-section">
	<h2 class="title big-text-title"><fmt:message key="about.tips"/></h2>
	<div class="big-text">
		<p><fmt:message key="about.tips.solved"/></p>
		<p><fmt:message key="about.tips.avoid.thanks"/></p>
		<p><fmt:message key="about.tips.kkk"/></p>
		<p><fmt:message key="about.tips.greetings"/></p>
	</div>
</div>

<div class="about-section">
	<h2 class="title big-text-title"><fmt:message key="about.reputation.permission" /></h2>
	<div class="big-text">
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
				<fmt:message key="about.reputation.answer_own_question">
					<fmt:param value="${ANSWER_OWN_QUESTION}"/>
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
			<li>
				<fmt:message key="about.reputation.interact_inactive_question">
					<fmt:param value="${INACTIVE_QUESTION}"/>
				</fmt:message>
			</li>
		</ul>
	</div><!-- big-text -->
</div><!-- about-section -->

<div class="about-section">
	<h2 class="title big-text-title"><fmt:message key="about.reputation.gain" /></h2>
	<div class="big-text">
		<ul class="rules-list">
			<li>
				<fmt:message key="about.reputation.gain.answer.solution">
					<fmt:param value="${SOLUTION_AUTHOR}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.answer.voted_up">
					<fmt:param value="${MY_ANSWER_VOTED_UP}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.question.voted_up">
					<fmt:param value="${MY_QUESTION_VOTED_UP}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.question.solution" >
					<fmt:param value="${SOLVED_QUESTION_AUTHOR}"/>
				</fmt:message>
			</li>
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
				<fmt:message key="about.reputation.gain.approved_edit">
					<fmt:param value="${APPROVED_INFORMATION}"/>		
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.comment.voted_up">
					<fmt:param value="${COMMENT_VOTED_UP}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.question_and_answer.voted_down">
					<fmt:param value="${MY_ANSWER_VOTED_DOWN}"/>
				</fmt:message>
			</li>
			<li>
				<fmt:message key="about.reputation.gain.question_and_answer.voted_down.me">
					<fmt:param value="${DOWNVOTED_QUESTION_OR_ANSWER}"/>
				</fmt:message>
			</li>			
		</ul>
	</div><!-- big-text -->
</div><!-- about-section -->

<div class="about-section">
	<h2 class="title big-text-title" id="faq"><fmt:message key="about.reputation.faq" /></h2>
	<div class="big-text">
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
			<li>
				<p id="perdi-meus-pontos" class="about-question"><fmt:message key="about.faq.where_is_my_karma"/></p>
				<p class="about-answer"><fmt:message key="about.faq.where_is_my_karma.answer"/></p>
			</li>
		</ul>
	</div>
</div>