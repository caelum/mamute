<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="time" required="true" type="org.joda.time.base.AbstractInstant"%>
<%@attribute name="pattern" required="true" type="java.lang.String"%>
<%
org.joda.time.format.DateTimeFormatter dateFormat = org.joda.time.format.DateTimeFormat.forPattern(pattern);
	if (time != null) {
		out.write(dateFormat.print(time));
	}
%>