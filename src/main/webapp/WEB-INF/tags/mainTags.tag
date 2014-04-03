<%@attribute name="tagClass" type="java.lang.String" required="false" %>
<%@attribute name="tagClassLi" type="java.lang.String" required="false" %>
<%@attribute name="useSprite" type="java.lang.Boolean" required="false" %>
<%@attribute name="unansweredTagLinks" type="java.lang.Boolean" required="false"%>
<%@attribute name="currentQuestion" type="org.mamute.model.Question" required="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:if test="${empty unansweredTagLinks}">
	<c:set value="${false}" var="unansweredTagLinks" />
</c:if>

<c:set value="${unansweredTagLinks ? true : false}" var="append" />

<ol class="main-tags ${tagClass}">
	<c:forTokens items="java, android, c#, .net, javascript, php, jquery, html, sql" delims=", " var="tagName">
		<li>
			<c:set value="${tag.name == tagName || currentQuestion.mostImportantTag.name == tagName  && useSprite ? 'main-tags-current' : ''}" var="currentTag"/>
			<c:set value="${useSprite ? 'main-tags-sprite main-tags-'.concat(fn:replace(fn:replace(tagName, '.', ''), '#', '')) : '' }" var="className"/>
			
			<a class="${currentTag} ${tagClassLi} ${className}"  href="${linkTo[ListController].withTag(tagName, 1, append)}"> ${tagName} </a>
		</li>
	</c:forTokens>
</ol>
