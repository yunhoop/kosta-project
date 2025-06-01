$(document).ready(function () {
  const postId = $("#postId").val();
  const $editBtn = $("#edit-btn");

  if ($editBtn.length && postId) {
    $editBtn.on("click", function () {
      window.location.href = `postEdit.html?postId=${postId}`;
    });
  }
});
