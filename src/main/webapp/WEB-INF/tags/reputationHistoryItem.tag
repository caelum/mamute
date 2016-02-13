<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@attribute name="historyItem" type="org.mamute.dto.KarmaAndContext" required="true" %>

<li class="ellipsis advanced-data-line">	
	<c:set var="reputationClass" value="${historyItem.karma > 0 ? 'positive-karma' : historyItem.karma < 0 ? 'negative-karma' : 'neutral-karma'}"/>
	<span class="reputation-won">
		<span class="counter karma-value ${reputationClass}">${historyItem.karma > 0 ? '+' : ''}${historyItem.karma}</span>
	</span>
	<jsp:doBody/> 
	<tags:contextLinkFor context="${historyItem.context}" />
</li>
