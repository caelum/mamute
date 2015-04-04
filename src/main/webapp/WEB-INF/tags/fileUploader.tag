<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="question" type="org.mamute.model.Question" required="false"%>

<c:if test="${env.get('feature.inhouse.upload')}">
    <div class="uploader">
        <label>Attachments - <a class="add-file" href="#">New file</a></label>
        <table class="uploaded-files ${empty question.attachments ? 'hidden':'' }">
            <tr>
                <th>${t['question.attachments.name']}</th>
                <th></th>
                <th></th>
            </tr>
            <c:forEach items="${question.attachments}" var="attachment">
                <tr id="attachment-${attachment.id}">
                    <td>${attachment.fileName()}</td>
                    <td>
                        <a href="/question/attachments/${attachment.id}">
                            /question/attachments/${attachment.id}
                        </a>
                    </td>
                    <td>
                        <a class="remove-attachment" data-attachment-id="${attachment.id}" href="#">
                            Remove
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
                <p>Choose your file to upload:</p>
                <a href="#" class="cancel-upload">Cancel</a></div>
        </div>
    </div>
</c:if>