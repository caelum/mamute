<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="count" type="java.lang.Integer" required="true" %>
<%@attribute name="key" type="java.lang.String" required="true" %>
<%@attribute name="information" type="java.lang.String" required="true" %>
<%@attribute name="htmlClass" type="java.lang.String" required="false" %>

<div class="info ${information} ${htmlClass}">${count}
	<div class="subtitle"><tags:pluralize key="${key}" count="${count}" /></div>
</div>