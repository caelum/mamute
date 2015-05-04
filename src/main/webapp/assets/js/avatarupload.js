if (Globals.inHouseUploading) {

    $(".profile-photo-upload").on("change", function(e) {
        var input = $(this);
        input.hide();
        var file = FileAPI.getFiles(e)[0];
        var uploading = $("<img>").attr("src", "/imgs/loading.gif");

        FileAPI.upload({
            url: '/users/' + $(this).data('user-id') + '/avatar',
            files: {avatar: file},
            complete:  function (err, xhr) {
                var attachment = JSON.parse(xhr.response);
                input.show();
                uploading.remove();
                $(".profile-image").attr("src", "/attachments/" + attachment.id + "?w=128&h=128")
            }
        });
        input.parent().append(uploading);
    });


}