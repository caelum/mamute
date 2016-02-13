<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@attribute name="vote" type="org.mamute.model.Vote" required="false" %>
<%@attribute name="flag" type="org.mamute.model.Flag" required="false" %>

<c:set var="isFlag"		value="${not empty flag}"/>
<c:set var="detail"		value="${isFlag ? flag : vote}"/>
<c:set var="quality"	value="${isFlag || !detail.isUp()  ? 'negative-karma' : 'positive-karma'}"/>
<c:set var="markText"	value="${isFlag ? 'X' : (detail.isUp()  ? '+' : '-')}"/>
<c:set var="altText"	value="${isFlag ? detail.type.toString() : ''}"/>


<li class="ellipsis advanced-data-line">
	<span class="reputation-won">
		<span class="counter karma-value ${quality} centered-karma" title="${altText}">${markText}</span>
	</span>
	<jsp:doBody/> 
	<tags:userProfileLink user="${detail.author}"/>
</li>
