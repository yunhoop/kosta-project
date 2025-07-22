$(document).ready(function () {
  const postId = new URLSearchParams(location.search).get("postId");
  
  $("#delete-btn").on("click", function () {
    if (confirm("정말 삭제하시겠습니까?")) {
      $.ajax({
        url: "controller",
        method: "POST",
        data: {
          cmd: "deletePostAction",
          postId: postId	
        },
        
        success: function (data) {
          if (data.result === "success") {
            alert("삭제가 완료되었습니다.");
            window.location.href = "mainUI.html";
          } else {
            alert("삭제 실패");
          }
        },
        error: function () {
          alert("서버 오류");
        }
      });
    }
  });
});
