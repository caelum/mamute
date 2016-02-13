<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@attribute name="uri" required="true" type="java.lang.String"%>
<%@attribute name="question" required="true" type="org.mamute.model.Question"%>

<c:set var="user" value="${currentUser.current}"/>
<c:set var="isAuthor" value="${question.author eq user}"/>
<c:set var="canAnswerIfOwnQuestion" value="${!isAuthor || user.hasKarmaToAnswerOwnQuestion(environmentKarma)}"/>
<c:set var="canAnswerIfQuestionIsInactive" value="${!question.isInactiveForOneMonth() || user.hasKarmaToAnswerInactiveQuestion(environmentKarma)}"/>
<c:set var="hasKarmaToAnswer" value="${canAnswerIfOwnQuestion && canAnswerIfQuestionIsInactive}"/>

<c:if test="${currentUser.loggedIn}">
		<c:if test="${!question.alreadyAnsweredBy(user) && hasKarmaToAnswer}">
			<tags:answerForm uri="${uri}" />
		</c:if>
		<c:if test="${question.alreadyAnsweredBy(user)}">
			<div class="message alert already-answered">
				${t['answer.errors.already_answered']}
			</div>
		</c:if>
		<c:if test="${!hasKarmaToAnswer}">
			<div class="message alert not-enough-karma">
				<c:if test="${isAuthor}">
					${t['answer.errors.not_enough_karma.own_question'].args(linkTo[NavigationController].about, linkTo[QuestionController].newQuestion)}
				</c:if>
				<c:if test="${!isAuthor}">
					${t['answer.errors.not_enough_karma'].args(linkTo[NavigationController].about, linkTo[QuestionController].newQuestion)}
				</c:if>
			</div>
		</c:if>
</c:if>


