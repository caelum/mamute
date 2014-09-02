<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="type" required="true" type="java.lang.String" %>
<%@attribute name="item" type="org.mamute.model.interfaces.Votable" required="true" %>
<%@attribute type="org.mamute.model.Vote" name="vote" required="true" %>
<div class="vote-container post-vote">
	<a rel="nofollow" class="container requires-login requires-karma author-cant
		      up-vote up-arrow arrow vote-option 
		       ${(not empty vote and vote.countValue == 1) ? 'voted' : '' }"
		      data-value="positivo" data-author="${currentUser.current.isAuthorOf(item)}"
		      data-type="${type}"
		      data-karma="${VOTE_UP}"
		      data-id="${item.id}"
		      title="${t['${type}.upvote']}">
    	up
   	</a>
	<span class="vote-count post-vote-count">${item.voteCount}</span>
	<a rel="nofollow" class="container requires-login author-cant down-vote down-arrow
	 		  arrow vote-option requires-karma
	 		  ${(not empty vote and vote.countValue == -1) ? 'voted' : '' }" 
	 		  data-value="negativo"  
	 		  data-author="${currentUser.current.isAuthorOf(item)}"
	 		  data-type="${type}" 
		      data-karma="${VOTE_DOWN}" 
	 		  data-id="${item.id}"
	 		  title="${t[type+'.downvote'].(MY_ANSWER_VOTED_DOWN, DOWNVOTED_QUESTION_OR_ANSWER)}">
		down 
	</a>
</div>