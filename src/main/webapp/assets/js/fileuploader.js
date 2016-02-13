if (Globals.inHouseUploading) {
    $(function() {
        $(".add-file").click(function(e) {
            e.preventDefault();
            createUploader();
        });

        function removeAttachment(e) {
            e.preventDefault();
            var link = $(this);
            link.css("pointer-events", "none");
            var id = link.data("attachment-id");
            $.ajax({
                url: Globals.linkTo.deleteAttachment + id,
                method: 'POST',
                data: { _method: 'DELETE' },
                success: function(result) {
                    $("#attachment-" + id).remove();
                    $("#input-attachment-" + id).remove();
                    Globals.markdownEditor.refreshPreview();
                }
            });
        }

        Globals.removeAttachment = removeAttachment;
        $(".remove-attachment").on("click", removeAttachment);
    });

    function createUploader(chunk, postProcessing) {
        var uploader = $(".attachment-uploader").show();
        $(".cancel-upload").click(function(e){
            e.preventDefault();
            uploader.hide();
        });

        uploader.find("input").remove();
        var uploaderContent = uploader.find(".upload-content");
        var uploadInput = $("<input type='file'>");
        uploaderContent.find(".file-input")
            .append(uploadInput);
        uploaderContent.find(".error").remove();
        uploaderContent.find("img").remove();
        var uploadCompleted = function (err, xhr) {
            if (err) {
                var error = $("<p class='error' style='margin:0'>")
                    .text("An error occurred during upload");
                uploaderContent.prepend(error);
            } else {
                var attachment = JSON.parse(xhr.response);
                var attachmentId = $("<input>")
                    .attr("type" ,"hidden")
                    .attr("name", "attachmentsIds[]")
                    .attr("value", attachment.id)
                    .attr("id", "input-attachment-" + attachment.id);
                $(".form-with-upload").append(attachmentId);
                addUploadedFile(attachment);
                if (chunk) {
                    var commandProto = Globals.markdownCommandManager;
                    commandProto.doCustomImage(chunk, postProcessing, true,
                        Globals.linkTo.getAttachment + attachment.id, null, attachment.name);
                }
                uploader.hide();
                Globals.markdownEditor.refreshPreview();
                $("#wmd-preview").removeClass("hidden");
            }
        };

        var onUpload = function (e) {
            var file = FileAPI.getFiles(e)[0];

            var filenames = $.map($('.attachment-name'), function( val, i ) {
                return val.innerText;
            });

            if (filenames.indexOf(file.name) >= 0) {
                var error = $("<p class='error' style='margin:0'>").text("File names must be unique");
                uploaderContent.prepend(error);
                return;
            }

            FileAPI.upload({
                url: Globals.linkTo.uploadAttachment,
                files: {file: file},
                complete: uploadCompleted
            });
            $(this).unbind(e);
            uploadInput.remove();
            uploaderContent.append($("<img>").attr("src", "/imgs/loading.gif"));
        };
        uploadInput.change(onUpload);

        var addUploadedFile = function(attachment) {
            var line = $("<tr>");
            var id = attachment.id;
            line.attr("id", "attachment-" + id);

            line.append($("<td class='attachment-name'>").text(attachment.name));
            line.append($("<td>").append(attachmentLink()));
            line.append($("<td>").append(removeLink(attachment)));

            $(".uploaded-files").append(line);
            $(".uploaded-files").removeClass("hidden");

            function removeLink(attachment) {
                return $("<a href='#'>").text("Remove")
                    .attr("data-attachment-id", attachment.id)
                    .click(Globals.removeAttachment)
                    .addClass("remove-attachment");
            }
            function attachmentLink() {
                return $("<a>")
                    .attr("href", Globals.linkTo.getAttachment + attachment.id)
                    .attr("target", "_blank")
                    .text(Globals.linkTo.getAttachment + attachment.id);
            }

        }
    }
    Globals.doimage = createUploader;
} else {
    Globals.doimage = function (chunk, postProcessing) {
        var commandProto = Globals.markdownCommandManager;
        filepicker.setKey(INK_API_KEY);
        var fp;
        var featherEditor = new Aviary.Feather({
            apiKey: AVIARY_API_KEY,
            apiVersion: 2,
            tools: 'crop,resize,draw,text',
            fileFormat: 'jpg',
            onClose: function(isDirty){
                if(isDirty){
                    filepicker.remove(fp);
                }
            },
            onSave: function(imageID, newURL) {
                filepicker.storeUrl(
                    newURL,
                    function(FPFile){
                        filepicker.remove(
                            fp,
                            function(){
                                commandProto.doLinkOrImage(chunk, postProcessing, true, FPFile.url);
                            }
                        );
                    }
                );

                featherEditor.close();
            },

            language: 'pt_BR'
        });

        var preview = document.getElementById('image-editor-preview');

        filepicker.pick({
            mimetype: 'image/*',
            container: 'modal',
            maxSize: 400*1024,
            services: ['COMPUTER', 'URL']
        },

        function(fpfile){
            fp = fpfile;
            preview.src = fpfile.url;
            featherEditor.launch({
                image: preview,
                url: fpfile.url
            });
        });
    };
}