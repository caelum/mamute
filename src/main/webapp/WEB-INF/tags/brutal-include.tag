<%@tag import="java.io.File"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="value" type="java.lang.String" required="true" %>

<% 
File customTemplate = new File(application.getRealPath("/WEB-INF/jsp/theme/custom/" + value));
if (customTemplate.exists()) { %>
	<jsp:include page="/WEB-INF/jsp/theme/custom/${value}" />
<%
} else { %>
	<jsp:include page="/WEB-INF/jsp/theme/default/${value}" />
<%
} %>
