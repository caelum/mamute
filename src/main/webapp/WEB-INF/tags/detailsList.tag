<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@attribute name="votable" type="br.com.caelum.brutal.model.interfaces.Votable" required="true" %>

<c:set var="isQuestion" value="${votable.class.name == 'br.com.caelum.brutal.model.Question'}" />

<section>
	<ul>
		<li>
			<b>${votable.author.name}</b>
		</li>
		
		<c:if test="${votable.votes.size == 0}">
			No votes
		</c:if>
		<c:forEach items="${votable.votes}" var="vote">
			<li>
				${vote.lastUpdatedAt} - ${vote.type} - ${vote.author.name}
			</li>
		</c:forEach>
	</ul>
</section>