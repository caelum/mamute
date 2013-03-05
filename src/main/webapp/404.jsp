<%@ page isErrorPage="true"  language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="not_found.title"/></title>
</head>
<body>
	<div class="subheader">
		<h2 class="title page-title"><fmt:message key="not_found.title"/></h2>
	</div>
	
	<div class="not-found">
		<p><fmt:message key="not_found.message"/>:</p> 
		
		<ul>
			<li><fmt:message key="not_found.recent_questions"><fmt:param value="${linkTo[ListController].home}"/></fmt:message></li>
			<li><fmt:message key="not_found.search"><fmt:param value="${linkTo[SearchController].search}"/></fmt:message></li>
			<li><fmt:message key="not_found.new_question"><fmt:param value="${linkTo[QuestionController].questionForm}"/></fmt:message></li>
		</ul>
	</div>
</body>
</html>