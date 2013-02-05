<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="type" required="true" type="java.lang.String" %>
<%@attribute name="item" type="br.com.caelum.brutal.model.Votable" required="true" %>
<%@attribute type="br.com.caelum.brutal.model.Vote" name="vote" required="true" %>
<c:if test="${item.author.id != currentUser.id }">
<div class="vote">
	<p>(votes <span class="vote-count">${answer.voteCount}</span>)<p>
	<a class="up-vote vote-option ${(not empty vote and vote.value==1) ? "voted" : "" }" data-value="up" data-type="${type}" data-id="${item.id}">up</a><br />
	<a class="down-vote vote-option ${(not empty vote and vote.value==-1) ? "voted" : "" }"" data-value="down" data-type="${type}" data-id="${item.id}">down</a>
	<p class="already-voted"><fmt:message key="vote.already_voted"/></p>
</div>
</c:if>
