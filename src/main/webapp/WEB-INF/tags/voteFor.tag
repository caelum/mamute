<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@attribute name="type" required="true" type="java.lang.String" %><%@attribute name="item" type="br.com.caelum.brutal.model.Votable" required="true" %><%@attribute type="br.com.caelum.brutal.model.Vote" name="vote" required="true" %>
<c:if test=${item.author.id not eq currentUser.id }>
<div class="vote">
	<a class="up-vote vote-option ${(not empty vote and vote.value==1) ? "voted" : "" }" data-value="up" data-type="${type}" data-id="${item.id}">up</a><br />
	<a class="down-vote vote-option ${(not empty vote and vote.value==-1) ? "voted" : "" }" data-value="down" data-type="${type}" data-id="${item.id}">down</a>
</div>
</c:if>