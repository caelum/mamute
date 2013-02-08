<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>
<section class="post-area question-area">
	<div class="post-meta">
		<tags:voteFor item="${question}" type="question" vote="${currentVote }"/>
	</div>
	<div class="post-container">
		<div class="post-text" id="question-description-${question.id }">${question.markedDescription}</div>
		<tags:tagsFor question="${question}"/>
		<ul class="post-action-nav nav">
			<li class="nav-item">
				<a class="post-action" href="<c:url value="/question/edit/${question.id}"/>"><fmt:message key="edit" /></a>
			</li>
		</ul>
		<tags:add-a-comment item="${question}" />
	</div>
</section>
