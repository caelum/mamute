<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@attribute name="vote" type="br.com.caelum.brutal.model.Vote" required="true" %>

<li class="ellipsis advanced-data-line">
	<c:set var="voteClass" value="${vote.isUp()  ? 'positive-karma' : 'negative-karma'}"/>
	<span class="reputation-won">
		<span class="counter karma-value ${voteClass} centered-karma">${vote.isUp()  ? '+' : '-'}</span>
	</span>
	<jsp:doBody/> 
	<a href="${linkTo[UserProfileController].showProfile(vote.author, vote.author.sluggedName)}">${vote.author.name}</a>
</li>
