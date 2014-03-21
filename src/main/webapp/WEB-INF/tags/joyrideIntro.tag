<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<ol id="intro">
	<tags:joyrideTip className="votes" options="tipLocation:top" key="intro.home.votes" />
	<tags:joyrideTip className="answers" options="tipLocation:top" key="intro.home.answers" />
	<tags:joyrideTip className="solution-mark" options="tipLocation:right" key="intro.home.solution_mark" />
	<tags:joyrideTip className="main-tags" options="tipLocation:bottom" key="intro.home.tags" />
	<tags:joyrideTip className="about" options="tipLocation:bottom" key="intro.about" />
</ol>