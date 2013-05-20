<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="hintId" required="true" type="java.lang.String"%>
<%@attribute name="htmlClass" required="false" type="java.lang.String"%>
<%@attribute name="placeholder" required="false" type="java.lang.String"%>
<%@attribute name="minlength" required="false" type="java.lang.Long"%>
<%@attribute name="maxlength" required="false" type="java.lang.Long"%>

<div class="wmd">
	<div class="wmd-panel">
		<div id="wmd-button-bar"></div>
<%-- 		<ul class="button-hint-nav nav">
			<li class="button-hint nav-item" data-button-hint-id="links-button-hint"><fmt:message key="button_hint.links.text"/></li>
			<li class="button-hint nav-item" data-button-hint-id="images-button-hint"><fmt:message key="button_hint.images.text"/></li>
			<li class="button-hint nav-item" data-button-hint-id="styles-header-button-hint"><fmt:message key="button_hint.styles_headers.text"/></li>
			<li class="button-hint nav-item" data-button-hint-id="lists-button-hint"><fmt:message key="button_hint.lists.text"/></li>
			<li class="button-hint nav-item" data-button-hint-id="blockquotes-button-hint"><fmt:message key="button_hint.blockquotes.text"/></li>
			<li class="button-hint nav-item" data-button-hint-id="codes-button-hint"><fmt:message key="button_hint.codes.text"/></li>
			<li class="button-hint nav-item" data-button-hint-id="html-button-hint"><fmt:message key="button_hint.html.text"/></li>
		</ul>
		<div class="hint-texts">
			<div id="links-button-hint" class="button-hint-text"><fmt:message key="button_hint.links.hint"/></div>
			<div id="images-button-hint" class="button-hint-text"><fmt:message key="button_hint.images.hint"/></div>
			<div id="styles-header-button-hint" class="button-hint-text"><fmt:message key="button_hint.styles_headers.hint"/></div>
			<div id="lists-button-hint" class="button-hint-text"><fmt:message key="button_hint.lists.hint"/></div>
			<div id="blockquotes-button-hint" class="button-hint-text"><fmt:message key="button_hint.blockquotes.hint"/></div>
			<div id="codes-button-hint" class="button-hint-text"><fmt:message key="button_hint.codes.hint"/></div>
			<div id="html-button-hint" class="button-hint-text"><fmt:message key="button_hint.html.hint"/></div>
		</div> --%>
		<textarea class="${htmlClass} hintable wmd-input" id="wmd-input"
			placeholder="${placeholder}"
			data-hint-id="${hintId}" 
			minlength="${minlength}"
			maxlength="${maxlength}"
			name="description"><c:out value="${value}" escapeXml="true"/></textarea>
	</div>
	<div class="md-panel md-preview hidden"></div>
	<div id="web_demo_pane">
		<img id="web_demo_preview" alt="Preview" style="display: none">
	</div>
</div>