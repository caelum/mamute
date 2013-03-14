<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="type" required="true" type="java.lang.String" %>
<%@attribute name="item" type="br.com.caelum.brutal.model.interfaces.Votable" required="true" %>
<%@attribute type="br.com.caelum.brutal.model.Vote" name="vote" required="true" %>
<div class="vote-container post-vote container">
	<a class="requires-login requires-karma author-cant
		      up-vote up-arrow arrow vote-option 
		      container ${(not empty vote and vote.countValue == 1) ? 'voted' : '' }" 
		      data-value="up" data-author="${item.author.id == currentUser.id}"
		      data-type="${type}" 
		      data-karma="10" 
		      data-id="${item.id}">
    	up
   	</a>
	<span class="vote-count post-vote-count">${item.voteCount}</span>
	<a class="requires-login author-cant down-vote down-arrow
	 		  arrow vote-option container requires-karma
	 		  ${(not empty vote and vote.countValue == -1) ? 'voted' : '' }" 
	 		  data-value="down"  
	 		  data-author="${item.author.id == currentUser.id}"
	 		  data-type="${type}" 
		      data-karma="50" 
	 		  data-id="${item.id}"> 
		down 
	</a>
</div>