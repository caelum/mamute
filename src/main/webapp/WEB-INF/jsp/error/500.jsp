<fmt:message key="metas.errors.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<div class="subheader">
	<h2 class="title page-title"><fmt:message key="internal_error.title"/></h2>
</div>

<c:if test="${env.name != 'production'}">	
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