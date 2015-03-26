$(function() {
    $(".add-file").click(function(e) {
        e.preventDefault();
        var $link = $(this);
        var uploads = $link.parent().find(".uploads");
        var item = $("<li>");
        uploads.append(item);

        var file = $("<input>")
            .attr("type", "file")
            .addClass("file-upload");
        item.append(file);

        var $removeLink = $("<a href='#' class='remove-file'>Remove</a>");
        $removeLink.click(function(e){
            e.preventDefault();
            item.remove();
        });

        item.append($removeLink);
    });
})