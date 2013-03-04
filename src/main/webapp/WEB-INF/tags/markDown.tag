<%@ tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="hintId" required="true" type="java.lang.String"%>
<%@attribute name="htmlClass" required="false" type="java.lang.String"%>
<%@attribute name="minlength" required="false" type="java.lang.Long"%>
<%@attribute name="maxlength" required="false" type="java.lang.Long"%>

<div class="wmd">
	<div class="wmd-panel">
		<div id="wmd-button-bar"></div>
		<textarea class="${htmlClass} hintable wmd-input text-input" id="wmd-input"
		data-hint-id="${hintId}" 
		minlength="${minlength}"
		maxlength="${maxlength}"
		name="description">${value}</textarea>
	</div>
	<div class="md-panel md-preview hidden"></div>
</div>