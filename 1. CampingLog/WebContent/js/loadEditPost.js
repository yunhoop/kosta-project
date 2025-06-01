$(document).ready(function () {
  const postId = new URLSearchParams(location.search).get("postId");
  console.log("postId =", postId);

  if (!postId) {
    alert("잘못된 접근입니다.");
    location.href = "groundPost.html";
    return;
  }

  $.ajax({
    url: "controller",
    method: "GET",
    data: {
      cmd: "getPostDetail",
      postId: postId
    },
    dataType: "json",
    success: function (data) {
    		  console.log("data =", data);        // 전체 구조 확인
    		  console.log("post =", data.post);   // 게시글 객체 확인
    		  console.log(" allTags =", data.allTags);
    		  console.log(" selectedTags =", data.selectedTags);

      const post = data.post;
      $("#postId").val(post.postId);
      $("#postTitle").val(post.postTitle);
      $("#postContents").val(post.postContents);
      $("#categoryId").val(post.categoryId);

      if (post.postImage) {
        $("#previewImg").attr("src", `upload/${post.postImage}`);
        $("#imageFileNameDisplay").val(post.postImage);

        $("<input>", {
          type: "hidden",
          name: "originalImageName",
          id: "originalImageName",
          value: post.postImage
        }).appendTo("#editForm");
      }

      const allTags = data.allTags;
      const selectedTags = data.selectedTags;

      const $tagContainer = $("#tagContainer").empty();
      const $rowTop = $("<div>").addClass("row row-top");
      const $rowBottom = $("<div>").addClass("row row-bottom");

      allTags.forEach((tag, index) => {
        const $btn = $("<button>")
          .attr("type", "button")
          .addClass("tag")
          .data("tagid", tag.tagId)
          .text(`#${tag.tagName}`);

        if (selectedTags.includes(tag.tagId)) {
          $btn.addClass("selected");

          $("<input>")
            .attr({
              type: "hidden",
              name: "tagId",
              value: tag.tagId,
              class: `hidden-tag-${tag.tagId}`
            })
            .appendTo("#selectedTags");
        }

        (index < 6 ? $rowTop : $rowBottom).append($btn);
      });

      $tagContainer.append($rowTop, $rowBottom);

      //  이 시점 이후에 submit 이벤트 바인딩
      $("#editForm").on("submit", function (e) {
        e.preventDefault();
        const formData = new FormData(this);

        $.ajax({
          url: "controller?cmd=updatePostAction",
          type: "POST",
          data: formData,
          processData: false,
          contentType: false,
          success: function (res) {
            if (res.result === "success") {
              alert("수정 완료!");
              window.location.href = `detailPost.html?postId=${formData.get("postId")}`;
            } else {
              alert("수정 실패");
            }
          },
          error: function () {
            alert("서버 오류");
          }
        });
      });
    },
    error: function () {
      alert("서버 오류");
    }
  });
});
