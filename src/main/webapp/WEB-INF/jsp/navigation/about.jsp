<c:set var="title" value="${t['metas.about.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<div class="about-section">
	<h2 class="title big-text-title">${t['about.tips']}</h2>
	<div class="big-text">
		<p>${t['about.tips.solved']}</p>
		<p>${t['about.tips.avoid.thanks']}</p>
		<p>${t['about.tips.kkk']}</p>
		<p>${t['about.tips.greetings']}</p>
	</div>
</div>

<div class="about-section">
	<h2 class="title big-text-title">${t['about.reputation.permission']}</h2>
	<div class="big-text">
		<ul class="rules-list">
			<li>
				${t['about.reputation.upvote'].args(VOTE_UP)}
			</li>
			<li>
				${t['about.reputation.flag'].args(CREATE_FLAG)}
			</li>
			<li>
				${t['about.reputation.edit.question'].args(EDIT_QUESTION)}
			</li>
			<li>
				${t['about.reputation.edit.answer'].args(EDIT_ANSWER)}
			</li>
			<li>
				${t['about.reputation.answer_own_question'].args(ANSWER_OWN_QUESTION)}
			</li>
			<li>
				${t['about.reputation.downvote'].args(VOTE_DOWN)}
			</li>
			<li>
				${t['about.reputation.moderate'].args(MODERATE_EDITS)}
			</li>
			<li>
				${t['about.reputation.interact_inactive_question'].args(INACTIVATE_QUESTION)}
			</li>
		</ul>
	</div><!-- big-text -->
</div><!-- about-section -->

<div class="about-section">
	<h2 class="title big-text-title">${t['about.reputation.gain']}</h2>
	<div class="big-text">
		<ul class="rules-list">
			<li>
				${t['about.reputation.gain.answer.solution'].args(SOLUTION_AUTHOR)}
			</li>
			<li>
				${t['about.reputation.gain.answer.voted_up'].args(MY_ANSWER_VOTED_UP)}
			</li>
			<li>
				${t['about.reputation.gain.question.voted_up'].args(MY_QUESTION_VOTED_UP)}
			</li>
			<li>
				${t['about.reputation.gain.question.solution'].args(SOLVED_QUESTION_AUTHOR)}
			</li>
			<li>
				${t['about.reputation.gain.question.answered'].args(ANSWERED_QUESTION)}
			</li>
			<li>
				${t['about.reputation.gain.question.asked'].args(ASKED_QUESTION)}
			</li>
			<li>
				${t['about.reputation.gain.approved_edit'].args(APPROVED_INFORMATION)}
			</li>
			<li>
				${t['about.reputation.gain.comment.voted_up'].args(COMMENT_VOTED_UP)}
			</li>
			<li>
				${t['about.reputation.gain.question_and_answer.voted_down'].args(MY_ANSWER_VOTED_DOWN)}
			</li>
			<li>
				${t['about.reputation.gain.question_and_answer.voted_down.me'].args(DOWNVOTED_QUESTION_OR_ANSWER)}
			</li>			
		</ul>
	</div><!-- big-text -->
</div><!-- about-section -->

<div class="about-section">
	<h2 class="title big-text-title" id="faq">${t['about.reputation.faq']}</h2>
	<div class="big-text">
		<ul class="about-faq">
			<li>
				<p class="about-question">${t['about.faq.what_to_ask.question']}</p>
				<p class="about-answer">${t['about.faq.what_to_ask.answer']}</p>
			</li>
			<li>
				<p class="about-question">${t['about.faq.what_not_to_ask.question']}</p>
				<p class="about-answer">${t['about.faq.what_not_to_ask.answer']}</p>
			</li>
			<li>
				<p class="about-question">${t['about.faq.first_page_questions.question']}</p>
				<p class="about-answer">${t['about.faq.first_page_questions.answer']}</p>
			</li>
			<li>
				<p class="about-question">${t['about.faq.edition.question']}</p>
				<p class="about-answer">${t['about.faq.edition.answer']}</p>
			</li>
			<li>
				<p class="about-question">${t['about.faq.bad_question.question']}</p>
				<p class="about-answer">${t['about.faq.bad_question.answer']}</p>
			</li>
			<li>
				<p id="perdi-meus-pontos" class="about-question">${t['about.faq.where_is_my_karma']}</p>
				<p class="about-answer">${t['about.faq.where_is_my_karma.answer']}</p>
			</li>
		</ul>
	</div>
</div>