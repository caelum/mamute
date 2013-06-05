<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="icon" required="true" type="java.lang.String" %>
<%@attribute name="key" required="true" type="java.lang.String" %>

<li class="how-it-works-item">
	<a href="${linkTo[QuestionController].questionForm}"><span class="${icon} how-it-works-icon icon-4x"></span></a>
	<p><a href="${linkTo[QuestionController].questionForm}"><fmt:message key="${key}"/></a></p>
</li>