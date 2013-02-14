<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="type" required="true" type="java.lang.String" %>
<%@attribute name="item" type="br.com.caelum.brutal.model.interfaces.Votable" required="true" %>
<%@attribute type="br.com.caelum.brutal.model.Vote" name="vote" required="true" %>
<div class="vote-container container">
	<a class="requires-login up-vote up-arrow arrow  vote-option ${(not empty vote and vote.value==1) ? 'voted' : '' }" data-value="up" data-type="${type}" data-id="${item.id}">up</a>
	<span class="vote-count">${item.voteCount}</span>
	<a class="requires-login down-vote  down-arrow arrow vote-option ${(not empty vote and vote.value==-1) ? 'voted' : '' }" data-value="down" data-type="${type}" data-id="${item.id}">down</a>
</div>
