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
                input.show();
                uploading.remove();
                if (xhr.status == 400) {
                    errorPopup(Messages.get('user_profile.edit.form.error.upload.image'), input, "center-popup");
                    return;
                } else if (xhr.status != 200) {
                    errorPopup(Messages.get('user_profile.edit.form.error.upload.unknown'), input, "center-popup");
                }
                var attachment = JSON.parse(xhr.response);
                $(".profile-image").attr("src", "/attachments/" + attachment.id + "?w=128&h=128")
            }
        });
        input.parent().append(uploading);
    });


}