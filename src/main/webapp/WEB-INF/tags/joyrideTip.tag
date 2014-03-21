<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="className" type="java.lang.String" required="true" %>
<%@attribute name="options" type="java.lang.String" required="true" %>
<%@attribute name="key" type="java.lang.String" required="true" %>
<li data-button="<fmt:message key="intro.next_tip"/>" data-class="${className}" data-options="${options}"><fmt:message key="${key}" /></li>
