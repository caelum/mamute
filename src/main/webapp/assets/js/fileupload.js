$(function() {
    $(".add-file").click(function(e) {
        e.preventDefault();
        var $link = $(this);
        var uploads = $link.parent().find(".uploads");
        var item = $("<li>");
        uploads.append(item);

        var uploadInput = $("<input>")
            .attr("type", "file")
            .addClass("file-upload");
        item.append(uploadInput);

        uploadInput.on("change", function(e) {
            var file = FileAPI.getFiles(e)[0];
            FileAPI.upload({
                url: '/questions/attachments',
                files: { file: file },
                complete: function (err, xhr) {
                    if (err) {
                        uploadInput.remove();
                        var error = $("<p>").text("An error occurred during upload");
                        item.append(error);
                    } else {
                        var attachment = JSON.parse(xhr.response);
                        var attachmentId = $("<input>")
                            .attr("type" ,"hidden")
                            .attr("name", "attachmentsIds[]")
                            .attr("value", attachment.id);
                        $(".question-form").append(attachmentId);
                    }
                    console.log(xhr);
                }
            });
        });

        var $removeLink = $("<a href='#' class='remove-uploadInput'>Remove</a>");
        $removeLink.click(function(e){
            e.preventDefault();
            item.remove();
        });

        item.append($removeLink);
    });
})