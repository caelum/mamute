<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="internal_error.title"/></title>
</head>
<body>
	<div class="subheader">
		<h2 class="title page-title"><fmt:message key="internal_error.title"/></h2>
	</div>

	<c:if test="${env.name == 'development'}">	
 		<pre class="stacktrace">${stacktrace}</pre>
	</c:if>
	<c:if test="${env.name == 'production'}">
		<!-- ${stacktrace} -->
		<div class="not-found">
			<p>Um erro inesperado ocorreu enquanto você visitava o nosso site. Pedimos desculpa pelo incômodo.</p>
			<p>Uma cópia deste erro foi enviada para nós e verificaremos o que ocorreu.</p>
		</div>
		<div class="error-code">
			500
		</div>
	</c:if>
</body>
</html>