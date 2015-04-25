<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="uri" required="true" type="java.lang.String"%>
<%@attribute name="edit" required="false" type="java.lang.String"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="siteName" value="${t['site.name']}"/>

<h2 class="title page-title subheader new-answer-title">${t['newanswer.answer.your_answer']}</h2>
<c:set var="sameAuthor" value="${question.author eq currentUser.current}" />
<c:set var="placeholder" value="${t['answer.form.placeholder']}"/>
<c:if test="${sameAuthor}">
	<c:set var="placeholder" value="${t['answer.form.sameauthor.placeholder']}"/>
	<div class="hidden same-author-confirmation hinted-form answer-form">
		<button class="post-submit big-submit">${t['newanswer.answer.add_own_answer']}</button>
	</div>
</c:if>

<form action="${uri}" method="post" class="validated-form form-with-upload ${not empty edit ? 'hinted-form' : '' } answer-form" data-same-author="${sameAuthor}">
	<tags:markDown placeholder="${placeholder}" value="${answer.description}" hintId="newanswer-answer-hint" htmlClass="required description-input" minlength="30"/>

    <c:if test="${env.supports('feature.inhouse.upload')}">
        <tags:fileUploader attachmentsTarget="${answer}"/>
    </c:if>
	
	<c:if test='${not empty edit}'>
		<label for="comment">${t['edit_form.comment.label']}</label>
		<input type="text" data-hint-id="answer-comment-hint" placeholder="${t['edit_form.comment.placeholder']}" class="hintable required text-input" length="5" name="comment" />
	</c:if>
	
	<div id="newanswer-answer-hint" class="hint">
		<c:choose> 
			<c:when test='${sameAuthor}'>
				<p>
					${t['newanswer.answer.sameauthor.hint'].args(siteName)}
				</p>
			</c:when>
			<c:otherwise>
				<p>
					${t['newanswer.answer.hint'].args(siteName)}
				</p>
			</c:otherwise>
		</c:choose>
	</div>
	
	<c:if test='${not empty edit}'>
		<div id="answer-comment-hint" class="hint">
			<p>${t['edit_form.comment.hint']}</p>
		</div>
	</c:if>
	
	<input class="post-submit big-submit submit" value="${t['newanswer.answer.submit']}" type="submit" />
	<c:if test='${empty edit}'>
		<tags:checkbox-watch/>	
	</c:if>
	
</form>