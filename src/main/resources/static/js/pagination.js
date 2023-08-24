function updatePagination(button) {
    var page = button.getAttribute('data-currentPage');
    var limit = button.getAttribute('data-limit');
    var newUrl = new URL(window.location.href);
    newUrl.searchParams.set("page", page);
    newUrl.searchParams.set("limit", limit);
    window.location.href = newUrl.toString();
}