$(function() {
    function executeDelete(form) {
        $("." + form).submit();
    }
    $(".delete-post").click(function(e) {
        e.preventDefault();
        var confirmation = true;
        var $link = $(this);
        var form = $link.data("delete-form");
        if ($link.data("confirm-deletion")) {
            confirmation = confirm("Are you sure that you want to delete this item?")
        }

        if (confirmation) {
            executeDelete(form);
        }
    })
});