$(function() {
    function executeDelete(url) {
        $.ajax({
            url: url,
            method: "DELETE",
            complete: function (xhr, status) {
                if (xhr.status == 200) {
                    document.location.href = "/?message=question.delete.confirmation";
                } else if (xhr.status == 400) {
                    alert(xhr.responseText);
                } else {
                    alert("An unknown error occurred");
                }
            }
        });
    }
    $(".delete-question").click(function(e) {
        e.preventDefault();
        var url = $(this).attr("href");
        var confirmation = true;
        if ($(this).data("confirm-deletion")) {
            confirmation = confirm("Are you sure that you want to delete this question?")
        }

        if (confirmation) {
            executeDelete(url);
        }
    })
});