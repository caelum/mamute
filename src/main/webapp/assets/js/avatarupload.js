if (Globals.inHouseUploading) {


    $(".profile-photo-upload").on("change", function(e) {
        var input = $(this);
        var file = FileAPI.getFiles(e)[0];
        var uploading = $("<img>").attr("src", "/imgs/loading.gif");

        FileAPI.upload({
            url: '/users/' + $(this).data('user-id') + '/avatar',
            files: {avatar: file},
            complete:  function (err, xhr) {
                var attachment = JSON.parse(xhr.response);
                console.log(attachment);
                uploading.remove();
            }
        });
        $(this).unbind(e);
        input.parent().append(uploading);
        input.remove();
    });


}