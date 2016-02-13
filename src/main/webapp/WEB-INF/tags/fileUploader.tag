<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="attachmentsTarget" type="org.mamute.model.Post" required="false"%>

<c:if test="${env.get('feature.inhouse.upload')}">
    <div class="uploader">
        <label>${t['question.attachments']} - <a class="add-file" href="#">${t['question.attachments.newfile']}</a></label>
        <table class="uploaded-files ${empty attachmentsTarget.attachments ? 'hidden':'' }">
            <tr>
                <th>${t['question.attachments.name']}</th>
                <th></th>
                <th></th>
            </tr>
            <c:forEach items="${attachmentsTarget.attachments}" var="attachment">
                <tr id="attachment-${attachment.id}">
                    <td>${attachment.fileName()}</td>
                    <td>
						<c:set var="attachmentLink" value="${linkTo[AttachmentController].downloadAttachment(attachment.id)}"/>
                        <a href="${attachmentLink}" target="_blank">
							${attachmentLink}
                        </a>
                    </td>
                    <td>
                        <a class="remove-attachment" data-attachment-id="${attachment.id}" href="#">
                            ${t['question.attachments.remove']}
                        </a>
                    </td>
                </tr>
                <input type="hidden" name="attachmentsIds[]"
                       id="input-attachment-${attachment.id}"
                       value="${attachment.id}">
            </c:forEach>
        </table>
        <div class="attachment-uploader hidden">
            <div class="upload-content">
                <p>${t['question.attachments.choose']}</p>
                <div class="file-input"></div>
                <a href="#" class="cancel-upload">${t['cancel_button']}</a></div>
        </div>
    </div>
</c:if>