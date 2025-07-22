$(document).ready(function () {
  $("#photoUploadBtn").on("click", function () {
    $("#photoInput").click();
  });

  $("#photoInput").on("change", function () {
    const formData = new FormData($("#profilePhotoForm")[0]);

    $.ajax({
      url: "controller?cmd=addProfilePhotoAction",
      method: "POST",
      data: formData,
      processData: false,
      contentType: false,
      success: function (data) {
        console.log("서버 응답:", data);

        // 응답이 JSON 문자열일 경우 직접 파싱 필요
        if (typeof data === "string") {
          data = JSON.parse(data);
        }

        if (data.result === "success" && data.fileName) {
          alert("프로필 사진이 변경되었습니다!");
          $("#profileImage").attr("src", "upload/profile/" + data.fileName + "?" + new Date().getTime());
        } else {
          alert("업로드 실패");
        }
      },
      error: function () {
        alert("서버 오류 발생");
      }
    });
  });
});
