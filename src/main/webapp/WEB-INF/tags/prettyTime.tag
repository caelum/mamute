<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@attribute
	name="time" required="true" type="org.joda.time.base.AbstractInstant"%>
<%
	if (time != null) {
		org.joda.time.base.AbstractInstant time = (org.joda.time.base.AbstractInstant) jspContext
				.getAttribute("time");
		org.ocpsoft.prettytime.PrettyTime formatter = (org.ocpsoft.prettytime.PrettyTime) request
				.getAttribute("prettyTimeFormatter");
		out.write(formatter.format(time.toDate()));
	}
%>