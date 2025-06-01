$(document).ready(function () {
  $("#email").on("input", function () {
    const email = $(this).val().trim();
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.(com|net|org|co\.kr|kr|edu)$/;

    if (!email) {
      $(".message-below").text("이메일을 입력해주세요.").css("color", "#d9534f").show();
    } else if (!emailRegex.test(email)) {
      $(".message-below").text("유효한 이메일 형식이 아닙니다.").css("color", "#d9534f").show();
    } else {
      $(".message-below").text("사용 가능한 이메일입니다.").css("color", "#5cb85c").show();
    }
  });
});
