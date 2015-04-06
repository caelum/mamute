$(function() {
    $(".delete-question").click(function(e) {
        e.preventDefault();
        var id = $(this).data("question-id");
        $.ajax({
            url: "/question/" + id,
            method: "DELETE"
        })
    })
});