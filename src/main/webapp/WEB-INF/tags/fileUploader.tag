<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${env.get('feature.inhouse.upload')}">
    <div class="uploader">
        <label>Attachments - <a class="add-file" href="#">New file</a></label>
        <table class="uploaded-files hidden">
            <tr>
                <th>${t['question.attachments.name']}</th>
                <th></th>
                <th></th>
            </tr>
        </table>
        <div class="attachment-uploader hidden">
            <div class="upload-content">
                <p>Choose your file to upload:</p>
                <a href="#" class="cancel-upload">Cancel</a></div>
        </div>
    </div>
</c:if>