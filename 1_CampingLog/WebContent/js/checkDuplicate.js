$(document).ready(function () {
	function checkDuplicate(field, value, messageSelector) {
		$.ajax({
			type: "GET",
			url: `/CampingLog/controller?cmd=checkDuplicate&field=${field}&value=${value}`,
			success: function (res) {
				let message = "";
				switch (field) {
					case "id":
						message = "아이디";
						break;
					case "email":
						message = "이메일";
						break;
					case "nickname":
						message = "닉네임";
						break;
				}

				if (res === "true") {
					$(messageSelector).hide(); // 중복 아님: 숨김
				} else {
					$(messageSelector).text(`${message} 중복`).css("color", "red").show(); // 중복: 다르게 표시
				}
			},
			error: function () {
				$(messageSelector).text("확인 중 오류").css("color", "gray").show();
			}
		});
	}

	$("#userid").on("blur", function () {
		const val = $(this).val();
		if (val) {
			checkDuplicate("id", val, $("#idCheckMsg"));
		}
	});

	$("#email").on("blur", function () {
		const val = $(this).val();
		if (val) {
			checkDuplicate("email", val, $("#emailCheckMsg"));
		}
	});

	$("#nickname").on("blur", function () {
		const val = $(this).val();
		if (val) {
			checkDuplicate("nickname", val, $("#nickNameCheckMsg"));
		}
	});

	// 비밀번호 일치 확인
	$("#confirmPassword, #password").on("keyup", function () {
		const pw1 = $("#password").val();
		const pw2 = $("#confirmPassword").val();
		const $msg = $("#pwMatchMsg");
		if (pw1 && pw2) {
			if (pw1 === pw2) {
				$msg.text("비밀번호가 일치합니다.").css("color", "green");
			} else {
				$msg.text("비밀번호가 일치하지 않습니다.").css("color", "red");
			}
		} else {
			$msg.text("");
		}
	});
});
