$(function() {
    function executeDelete(form) {
        $("." + form).submit();
    }
    $(document).on('click', '.delete-post, .delete-user', function(e) {
        e.preventDefault();
        var confirmation = true;
        var $link = $(this);
        var form = $link.data("delete-form");
        var type = $link.data("item-type") || "item";
        if ($link.data("confirm-deletion")) {
            confirmation = confirm("Are you sure that you want to delete this " + type + "?");
        }

        if (confirmation) {
            executeDelete(form);
        }
    });
});