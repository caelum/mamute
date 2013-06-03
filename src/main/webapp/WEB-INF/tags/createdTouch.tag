<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="touchable" type="br.com.caelum.brutal.model.interfaces.Touchable" required="true" %>
<tags:completeUser itemProp="dateCreated" touchText="touch.created" user="${touchable.author}" date="${touchable.createdAt}"/>
