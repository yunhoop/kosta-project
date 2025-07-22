$("#postForm").on("submit", function (e) {
  e.preventDefault();

  const formData = new FormData(this);

  $.ajax({
    url: "controller?cmd=writeAction",
    type: "POST",
    data: formData,
    processData: false,
    contentType: false,
    success: function (res) {
      if (res.result === "success") {
        window.location.href = "groundPost.html";
      } else {
        alert("글 등록 실패!");
      }
    },
    error: function () {
      alert("서버 오류 발생");
    }
  });
});
