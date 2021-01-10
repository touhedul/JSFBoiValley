
$(".showbook").on('click', function (e) {
    $("#book_modal").modal("show");
    var bookId = $(this).data();
    $.get("{{route('show.book')}}", {bookId: bookId}, function (data) {
        $("#name").val(data.name);
        $("#author_name").val(data.author_name);
        $("#department").val(data.department_id);
        $("#price").val(data.price);
        $("#year").val(data.year);
        $("#book_id").val(data.id);
    });
});