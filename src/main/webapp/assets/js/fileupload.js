$(function() {
    $(".add-file").click(function(e) {
        e.preventDefault();
        var $link = $(this);
        var uploadWidget = $("<div>").addClass("attachment-uploader");
        var uploadContent = $("<div>").addClass("upload-content");
        uploadWidget.append(uploadContent);
        uploadContent.append($("<p>Choose your file to upload:</p>"))

        var uploadInput = $("<input>")
            .attr("type", "file")
            .addClass("file-upload");
        uploadContent.append(uploadInput);

        var cancelUpload = $("<a href='#' class='cancel-upload'>Cancel</a>");
        cancelUpload.click(function(e){
            e.preventDefault();
            uploadWidget.remove();
        });

        uploadContent.append(cancelUpload);
        $(".question-form").append(uploadWidget);

        var uploadCompleted = function (err, xhr) {
            uploadInput.remove();
            if (err) {
                var error = $("<p class='error'>").text("An error occurred during upload");
                uploadContent.append(error);
            } else {
                var attachment = JSON.parse(xhr.response);
                var attachmentId = $("<input>")
                    .attr("type" ,"hidden")
                    .attr("name", "attachmentsIds[]")
                    .attr("value", attachment.id)
                    .attr("id", "input-attachment-" + attachment.id);
                $(".question-form").append(attachmentId);
                addUploadedFile(attachment);
                uploadWidget.remove();
            }
        };

        var onUpload = function (e) {
            var file = FileAPI.getFiles(e)[0];
            FileAPI.upload({
                url: '/questions/attachments',
                files: {file: file},
                complete: uploadCompleted
            });
        };
        uploadInput.on("change", onUpload);

        var addUploadedFile = function(attachment) {
            var line = $("<tr>");
            var id = attachment.id;
            line.attr("id", "attachment-" + id);

            line.append($("<td>").text(attachment.name))

            line.append($("<td>").append(removeLink(attachment)));

            var addToQuestion = $("<a>").text("Add to question")
                .attr("data-attachment-id", id)
                .click(putInQuestionContent);
            line.append($("<td>").append(addToQuestion));

            $(".uploaded-files").append(line);
            $(".uploaded-files").removeClass("hidden");

            function removeAttachment(e) {
                e.preventDefault();
                var link = $(this);
                link.css("pointer-events", "none");
                var id = link.data("attachment-id");
                $.ajax({
                    url: '/questions/attachments/' + id,
                    type: 'DELETE',
                    success: function(result) {
                        $("#attachment-" + id).remove();
                        $("#input-attachment-" + id).remove();
                    }
                });
            }

            function removeLink(attachment) {
                return $("<a href='#'>").text("Remove")
                    .attr("data-attachment-id", attachment.id)
                    .click(removeAttachment)
                    .addClass("remove-attachment");
            }
        }
    });

    function putInQuestionContent() {
        var link = $(this);
    }
});