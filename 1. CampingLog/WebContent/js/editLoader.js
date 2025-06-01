function getPostIdFromURL() {
  const params = new URLSearchParams(window.location.search);
  return params.get("postId");
}

$(document).ready(function () {
  const postId = getPostIdFromURL();

  if (!postId) {
    alert("잘못된 접근입니다.");
    window.location.href = "groundPost.html";
    return;
  }

  $.ajax({
    url: "controller",
    method: "GET",
    data: { cmd: "getPostDetail", postId: postId },
    success: function (data) {
      if (!data) return alert("게시글 데이터를 불러올 수 없습니다.");

      $("#postId").val(data.postId);
      $("#postTitle").val(data.postTitle);
      $("#postContents").val(data.postContents);
      $("#categoryId").val(data.categoryId);

      if (data.postImage) {
        $("#previewImg").attr("src", `upload/${data.postImage}`);
      }

      const $tagContainer = $("#tagContainer").empty();
      const $rowTop = $("<div>").addClass("row row-top");
      const $rowBottom = $("<div>").addClass("row row-bottom");

      data.allTags.forEach((tag, index) => {
        const $btn = $("<button>")
          .attr("type", "button")
          .addClass("tag")
          .data("tagid", tag.tagId)
          .text(`#${tag.tagName}`);

        if (data.selectedTags.includes(tag.tagId)) {
          $btn.addClass("selected");

          const $hidden = $("<input>")
            .attr({
              type: "hidden",
              name: "tagId",
              value: tag.tagId,
              class: `hidden-tag-${tag.tagId}`
            });

          $("#selectedTags").append($hidden);
        }

        (index < 6 ? $rowTop : $rowBottom).append($btn);
      });

      $tagContainer.append($rowTop, $rowBottom);
    },
    error: function () {
      alert("서버 오류로 데이터를 불러올 수 없습니다.");
    }
  });
});
