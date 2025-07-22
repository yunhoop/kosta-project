$(document).ready(function () {

  // 로그인된 회원 정보 표시
  $.ajax({
    url: "controller",
    method: "GET",
    data: { cmd: "getMyInfo" },
    dataType: "json",
    success: function (data) {
      if (data && data.memberId) {
        $("#memberIdDisplay").text(data.memberId);
        $("#email").val(data.email);
        $("#nickname").val(data.nickName);
        $("#name").val(data.name);
        $("#phoneNumber").val(data.phoneNumber);

        // 한 번만 초기 포커스 시 비우기
        ["#email", "#nickname", "#name", "#phoneNumber"].forEach(selector => {
          $(selector).one("focus", function () {
            $(this).val("");
          });
        });

      } else {
        alert("로그인 정보가 없습니다.");
        location.href = "login.html";
      }
    },
    error: function () {
      alert("회원 정보를 불러오는 데 실패했습니다.");
    }
  });

  // ✅ 이메일 중복 체크
  $("#email").on("blur", function () {
	  const email = $(this).val().trim();
	  if (!email) return;

	  $.ajax({
	    url: "controller",
	    method: "GET",
	    data: {
	      cmd: "checkDuplicate",
	      field: "email",
	      value: email
	    },
	    dataType: "json",
	    success: function (res) {
	      $(".message-inline").eq(0)
	        .text(res.exists ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.")
	        .css("color", res.exists ? "red" : "green");
	    },
	    error: function () {
	      $(".message-inline").eq(0).text("이메일 확인 실패").css("color", "gray");
	    }
	  });
	});


  // ✅ 닉네임 중복 체크
  $("#nickname").on("blur", function () {
	  const nickname = $(this).val().trim();
	  if (!nickname) return;

	  $.ajax({
	    url: "controller",
	    method: "GET",
	    data: {
	      cmd: "checkDuplicate",
	      field: "nickname",
	      value: nickname
	    },
	    dataType: "json",
	    success: function (res) {
	      $(".message-inline").eq(1)
	        .text(res.exists ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다.")
	        .css("color", res.exists ? "red" : "green");
	    },
	    error: function () {
	      $(".message-inline").eq(1).text("닉네임 확인 실패").css("color", "gray");
	    }
	  });
	});


  // 정보 수정
  $("#updateForm").on("submit", function (e) {
	  e.preventDefault();

	  const emailMessage = $(".message-inline").eq(0).text();
	  const nicknameMessage = $(".message-inline").eq(1).text();

	  // 중복 경고일 경우 제출 중단
	  if (emailMessage.includes("이미 사용 중") || nicknameMessage.includes("이미 사용 중")) {
	    alert("중복된 항목이 있습니다. 수정할 수 없습니다.");
	    return;
	  }

	  const formData = $(this).serialize();

	  $.ajax({
	    url: "controller?cmd=updateMemberAction",
	    method: "POST",
	    data: formData,
	    dataType: "json",
	    success: function (data) {
	      if (data.result === "success") {
	        alert("회원 정보가 성공적으로 수정되었습니다.");
	        window.location.href = "myPage.html";
	      } else if (data.result === "unauthorized") {
	        alert("로그인 정보가 없습니다. 다시 로그인 해주세요.");
	        window.location.href = "login.html";
	      } else {
	        alert("회원 정보 수정에 실패했습니다.");
	      }
	    },
	    error: function () {
	      alert("서버와 통신 중 오류가 발생했습니다.");
	    }
	  });
	});
  $(document).on("click", ".password-edit-btn", function () {
	  location.href = "passwordEdit.html";
	});

});
