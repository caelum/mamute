<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="className" type="java.lang.String" required="true" %>
<%@attribute name="options" type="java.lang.String" required="true" %>
<%@attribute name="key" type="java.lang.String" required="true" %>
<c:set var="siteName" value="${t['site.name']}"/>

<li data-button="${t['intro.next_tip']}" data-class="${className}" data-options="${options}">${t[key].args(siteName)}</li>
