<%@attribute name="tagClass" type="java.lang.String" required="false" %>
<%@attribute name="tagClassLi" type="java.lang.String" required="false" %>
<%@attribute name="useSprite" type="java.lang.Boolean" required="false" %>
<%@attribute name="unansweredTagLinks" type="java.lang.Boolean" required="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${empty unansweredTagLinks}">
	<c:set value="${false}" var="unansweredTagLinks" />
</c:if>

<c:if test="${unansweredTagLinks}">
	<c:set value="?sem-respostas=true" var="append" />
</c:if>

<ol class="main-tags ${tagClass}">
	<li class="${tagClassLi}" >
		<a class="${useSprite? 'main-tags-sprite main-tags-java' : '' }" href="<c:url value="/tag/java${append}" />">
			java
		</a>
	</li>
	<li class="${tagClassLi}" >
		<a class="${useSprite? 'main-tags-sprite main-tags-android' : '' }" href="<c:url value="/tag/android${append}" />">
			android
		</a>
	</li>
	<li class="${tagClassLi}" >
		<a class="${useSprite? 'main-tags-sprite main-tags-c' : '' }" href="<c:url value="/tag/c%23${append}" />">
			c#
		</a>
	</li>
	<li class="${tagClassLi}" >
		<a class="${useSprite? 'main-tags-sprite main-tags-net' : '' }" href="<c:url value="/tag/.net${append}" />">
			.net
		</a>
	</li>
	<li class="${tagClassLi}" >
		<a class="${useSprite? 'main-tags-sprite main-tags-javascript' : '' }" href="<c:url value="/tag/javascript${append}" />">
			javascript
		</a>
	</li>
	<li class="${tagClassLi}" >	
		<a class="${useSprite? 'main-tags-sprite main-tags-php' : '' }" href="<c:url value="/tag/php${append}" />">
			php
		</a>
	</li>
	<li class="${tagClassLi}" >
		<a class="${useSprite? 'main-tags-sprite main-tags-jquery' : '' }" href="<c:url value="/tag/jquery${append}" />">
			jquery
		</a>
	</li>
	<li class="${tagClassLi}" >
		<a class="${useSprite? 'main-tags-sprite main-tags-html' : '' }" href="<c:url value="/tag/html${append}" />">
			html
		</a>
	</li>
	<li class="${tagClassLi}" >
		<a class="${useSprite? 'main-tags-sprite main-tags-sql' : '' }" href="<c:url value="/tag/sql${append}"/>">
			sql
		</a>
	</li>
</ol>

