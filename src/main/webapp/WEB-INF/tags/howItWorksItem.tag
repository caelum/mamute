<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="icon" required="true" type="java.lang.String" %>
<%@attribute name="key" required="true" type="java.lang.String" %>

<li class="how-it-works-item">
	<a class="how-it-works-info" href="${linkTo[QuestionController].questionForm}"><span class="${icon} how-it-works-icon icon-3x"></span></a>
	<div class="how-it-works-subtitle-container">
		<a class="how-it-works-info how-it-works-subtitle" href="${linkTo[QuestionController].questionForm}">${t[key]}</a>
	</div>
</li>