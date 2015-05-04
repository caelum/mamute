$(function() {
    function executeDelete() {
        $(".delete-question-form").submit();
    }
    $(".delete-question").click(function(e) {
        e.preventDefault();
        var confirmation = true;
        if ($(this).data("confirm-deletion")) {
            confirmation = confirm("Are you sure that you want to delete this question?")
        }

        if (confirmation) {
            executeDelete();
        }
    })
});