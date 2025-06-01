$(document).ready(function () {
  $("#postForm").on("submit", function (e) {
    e.preventDefault();

    const formData = new FormData(this);

    $.ajax({
      url: "controller?cmd=writeAction",
      method: "POST",
      data: formData,
      processData: false, // FormData 처리 방지
      contentType: false, // 기본 content-type 사용
      success: function (data) {
        if (data === "success" || data.result === "success") {
          alert("게시글이 등록되었습니다!");
          window.location.href = "groundPost.html";
        } else {
          alert("게시글 등록 실패");
        }
      },
      error: function () {
        alert("서버 오류 발생");
      }
    });
  });
});
