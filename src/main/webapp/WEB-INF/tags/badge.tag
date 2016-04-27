<%@ tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="badge" type="org.mamute.model.BadgeCount" required="true" %>

<a href="" class="badge"><span class="badge-circle badge-${badge.badgeType.badgeClass.name().toLowerCase()}">&nbsp;</span>${t[badge.badgeType.id]}</a>
