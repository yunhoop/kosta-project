$(document).ready(function() {
  $("#login-form").on("submit", function(e) {
    e.preventDefault(); // 폼 기본 제출 막기

    $.ajax({
      url: "controller?cmd=login",
      type: "POST",
      data: $(this).serialize(),
      dataType: "json",
      success: function(res) {
        console.log("[디버깅] 서버 응답:", res);

        if (res.loginSuccess === true) {
          alert("로그인 성공했습니다! 메인 페이지로 이동합니다.");

          // ⭐ 여기서 바로 이동하지 말고 form 만들어서 POST로 이동
          const form = document.createElement("form");
          form.method = "POST";
          form.action = "mainUI.html"; // 이동할 페이지

          document.body.appendChild(form);
          form.submit();
          
        } else {
          alert("로그인 실패! 아이디 또는 비밀번호를 확인하세요.");
          window.location.href = "login.html"; 
        }
      },
      error: function(xhr, status, error) {
        console.error("[서버 통신 오류]", status, error);
        alert("서버 통신 오류가 발생했습니다.");
      }
    });
  });
});

