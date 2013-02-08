<%@ tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="hintId" required="true" type="java.lang.String"%>

<div class="wmd">
	<div class="wmd-panel">
		<div id="wmd-button-bar"></div>
		<textarea class="required hintable wmd-input text-input" id="wmd-input"
			data-hint-id="${hintId}" minlength="30"
			name="description">${value}</textarea>
	</div>
	<div id="wmd-preview" class="wmd-panel wmd-preview hidden"></div>
</div>