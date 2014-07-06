<%@tag import="java.io.File"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="value" type="java.lang.String" required="true"%>

<c:if test="${templateFinder.fileFinder(value) == true}"><jsp:include
		page="/WEB-INF/jsp/theme/custom/${value}.jspf" /></c:if>
<c:if test="${templateFinder.fileFinder(value) == false}"><jsp:include
		page="/WEB-INF/jsp/theme/default/${value}.jspf" /></c:if>
