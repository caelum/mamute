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
                url: '/question/attachments',
                files: { file: file },
                complete: function (err, xhr) {
                    console.log(err);
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