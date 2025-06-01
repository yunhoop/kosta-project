console.log("headerAuthUI.js 로드됨");

$(document).ready(function () {
  function updateAuthUI() {
    $.ajax({
      url: "controller",
      method: "GET",
      data: { cmd: "loginCheck" },
      success: function (data) {
        if (data.loggedIn) {
          console.log(data.loggedIn);
          console.log(data.profileImage);
          $('.authButton, .loginButton').hide();
          // 프로필 이미지 설정
          const src = data.profileImage
            ? `/CampingLog/img/${data.profileImage}`
            : `/CampingLog/img/duck.jpg`;
          $('.userInfo .profile-img').attr('src', src);
          $('.userInfo').show();
        } else {
          $('.authButton, .loginButton').show();
          $('.userInfo').hide();
        }
      },
      error: function () {
        console.error("로그인 상태 확인 실패");
      }
    });
  }

  updateAuthUI();

  // ⭐ 프로필 클릭 시 POST 방식으로 myPage.html 이동
  $(document).on('click', '.userInfo .profile-img', function () {
	  window.location.href = "/CampingLog/myPage.html";
  });

  // 로그아웃 버튼 클릭 이벤트
  $(document).on('click', '#logoutBtn', function () {
    $.ajax({
      url: "controller",
      method: "GET",
      data: { cmd: "logout" },
      success: function () {
        console.log("로그아웃 성공");
        location.reload();
      },
      error: function () {
        alert("로그아웃 실패");
      }
    });
  });
});
