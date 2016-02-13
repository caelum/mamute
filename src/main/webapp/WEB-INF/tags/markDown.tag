<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="hintId" required="false" type="java.lang.String"%>
<%@attribute name="htmlClass" required="false" type="java.lang.String"%>
<%@attribute name="placeholder" required="false" type="java.lang.String"%>
<%@attribute name="name" required="false" type="java.lang.String"%>
<%@attribute name="minlength" required="false" type="java.lang.Long"%>
<%@attribute name="maxlength" required="false" type="java.lang.Long"%>

<c:set var="siteName" value="${t['site.name']}"/>

<c:if test="${empty name}">
	<c:set var="name" value="description"/>
</c:if>

<div class="wmd">
	<div class="wmd-panel">
		<div id="wmd-button-bar"></div>
		<img id="image-editor-preview" alt="Preview" style="display: none">
		<textarea class="${htmlClass} hintable wmd-input" id="wmd-input"
			placeholder="${placeholder}"
			data-hint-id="${hintId}" 
			minlength="${minlength}"
			maxlength="${maxlength}"
			name="${name}"><c:out value="${value}" escapeXml="true"/></textarea>
	</div>
	<div id="wmd-preview" class="md-panel md-preview hidden"></div>
</div>