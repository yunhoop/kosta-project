$(document).ready(function () {
  // 입력 시마다 비밀번호 일치 확인
  $("#password, #confirmPassword").on("input", function () {
    const pw = $("#password").val();
    const confirm = $("#confirmPassword").val();
    const $msg = $(".message");

    if (!pw && !confirm) {
      $msg.hide();
      return;
    }

    if (pw === confirm) {
      $msg.text("비밀번호 일치").css("color", "green").show();
    } else {
      $msg.text("비밀번호 불일치").css("color", "red").show();
    }
  });

  // 비밀번호 변경 요청
  $(".form-box").on("submit", function (e) {
    e.preventDefault();

    const pw = $("#password").val();
    const confirm = $("#confirmPassword").val();

    if (pw !== confirm) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }

    $.ajax({
      url: "controller?cmd=updatePasswordAction", 
      method: "POST",
      data: { pw: pw },
      dataType: "json",
      success: function (res) {
        if (res.result === "success") {
          alert("비밀번호가 성공적으로 변경되었습니다.");
          window.location.href = "mainUI.html";
        } else {
          alert("비밀번호 변경에 실패했습니다.");
        }
      },
      error: function () {
        alert("서버 오류가 발생했습니다.");
      }
    });
  });
});
