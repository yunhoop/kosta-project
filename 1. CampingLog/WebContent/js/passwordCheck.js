$(document).ready(function () {
  const target = new URLSearchParams(location.search).get("target"); // "edit" or "delete"

  // 1. 로그인한 사용자 정보 불러오기
  $.getJSON("controller?cmd=getMyInfo", function (user) {
    if (user && user.memberId) {
      $("#memberIdDisplay").text(user.memberId);
      $("#hiddenMemberId").val(user.memberId);
    } else {
      alert("로그인이 필요합니다.");
      location.href = "login.html";
    }
  }).fail(function () {
    alert("서버 통신 오류가 발생했습니다.");
    location.href = "login.html";
  });

  // 2. 비밀번호 확인
  $(".form-box").on("submit", function (e) {
    e.preventDefault();

    const pw = $("#password").val().trim();
    if (!pw) {
      alert("비밀번호를 입력해주세요.");
      return;
    }

    $.ajax({
      url: "controller?cmd=passwordCheckAction",
      method: "POST",
      data: { pw: pw },
      dataType: "json",
      success: function (res) {
        if (res.verified) {
          if (target === "edit") {
            alert("비밀번호 확인 완료! 정보 수정 화면으로 이동합니다.");
            window.location.href = "myPageEdit.html";
          } else if (target === "delete") {
            const confirmed = confirm("정말 탈퇴하시겠습니까?");
            if (confirmed) {
              $.ajax({
                url: "controller?cmd=deleteMemberAction",
                type: "POST",
                data: { pw: pw },
                dataType: "json",
                success: function (result) {
                  if (result.result === "success") {
                    alert("회원 탈퇴가 완료되었습니다.");
                    location.href = "mainUI.html";
                  } else {
                    alert("탈퇴에 실패했습니다.");
                  }
                },
                error: function () {
                  alert("탈퇴 요청 중 오류가 발생했습니다.");
                }
              });
            }
          } else {
            alert("올바르지 않은 요청입니다.");
          }
        } else {
          alert("비밀번호가 틀렸습니다.");
        }
      },
      error: function () {
        alert("비밀번호 확인 요청 실패");
      }
    });
  });
});
