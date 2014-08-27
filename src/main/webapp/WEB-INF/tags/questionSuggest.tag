<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="titleId" required="true" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="containerId" value="question-suggestions"/>
<c:set var="listId" value="question-suggestion-items"/>

<div id="${containerId}">
	<h2 class="title section-title">Similar questions</h2>

	<div id="${listId}"></div>
</div>

<script src="<c:url value="/js/deps/jquery.js"/>"></script>
<script type="text/javascript">
	function getQuestionSuggestions(data) {
		if (data.trim() == "") {
			$("#${containerId}").hide();
			return;
		}

		$("#${containerId}").show();
		$("#${listId}").children().remove();
		$("#${listId}").html(data);
	}

	$("#${titleId}").keyup(function () {
		if ($("#${titleId}").val().length >= 5) {
			//keep from spamming the search endpoint on keypresses
			window.clearTimeout($(this).data("question-suggest-timeout"));
			$(this).data("question-suggest-timeout", setTimeout(function () {
				$.get("/searchAjax", {query: $("#${titleId}").val()}, getQuestionSuggestions);
			}, 1000));
		} else {
			$("#${containerId}").hide();
		}
	});
</script>