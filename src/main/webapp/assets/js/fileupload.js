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
                var error = $("<p>").text("An error occurred during upload");
                uploadWidget.append(error);
            } else {
                var attachment = JSON.parse(xhr.response);
                var attachmentId = $("<input>")
                    .attr("type" ,"hidden")
                    .attr("name", "attachmentsIds[]")
                    .attr("value", attachment.id);
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

            line.append($("<td>").text(attachment.name))

            var addToQuestion = $("<a>").text("Add to question")
                .attr("data-attachment-id", attachment.id)
                .click(putInQuestionContent);
            line.append($("<td>").append(addToQuestion));

            $(".uploaded-files").append(line);
        }
    });

    function putInQuestionContent() {
        var link = $(this);
        debugger
    }
});