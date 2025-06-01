console.log("SignUp.js 로드됨");

$(document).ready(function () {

	// 모든 .message 요소를 처음에 숨김
	$(".message").hide();

	// 1. 비밀번호 일치 확인
	$("#confirmPassword").on("input", function () {
		const pw = $("#password").val();
		const confirm = $(this).val();

		const $msg = $("#pwMatchMsg");
		if (pw === confirm) {
			$msg.text("비밀번호 일치").css("color", "green").show();
		} else {
			$msg.text("비밀번호 불일치").css("color", "red").show();
		}
	});

	// 2. 중복 확인 공통 함수 (JSON 응답 기준)
	function checkDuplicate(field, value, messageSelector) {
		const labelMap = {
			id: "아이디",
			email: "이메일",
			nickname: "닉네임"
		};

		const label = labelMap[field] || "항목";

		if (!value || value.trim() === "") {
			$(messageSelector).hide(); // 빈 값이면 메시지 숨김
			return; // 서버 요청 안 보냄
		}

		$.ajax({
			type: "GET",
			url: `/CampingLog/controller?cmd=checkDuplicate&field=${field}&value=${value}`,
			dataType: "json",
			success: function (res) {
				const $msg = $(messageSelector);
				if (res.exists === false) {
					$msg.text(`${label} 사용 가능`).css("color", "green").show();
				} else {
					$msg.text(`${label} 중복`).css("color", "red").show();
				}
			},
			error: function () {
				$(messageSelector).text(`${label} 확인 실패`).css("color", "gray").show();
			}
		});
	}


	// 3. 중복 확인 이벤트 바인딩
	$("#userid").on("blur", function () {
		console.log("아이디 필드 blur 이벤트 실행됨");
		checkDuplicate("id", $(this).val(), "#idCheckMsg");
	});
	$("#email").on("blur", function () {
		const email = $(this).val().trim();
		const $msgFormat = $(".message-below");
		const $msgDup = $("#emailCheckMsg");
		const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.(com|net|org|co\.kr|kr|edu)$/;

		if (!email) {
			$msgFormat.text("이메일을 입력해주세요").css("color", "red").show();
			$msgDup.hide(); // 중복확인 메시지 숨김
			return;
		}

		if (!emailRegex.test(email)) {
			$msgFormat.text("유효한 이메일 형식이 아닙니다").css("color", "red").show();
			$msgDup.hide(); // 중복확인 메시지 숨김
			return;
		}

		// 형식이 맞으면 하단 형식 오류 메시지 숨기고 중복확인 수행
		$msgFormat.hide();
		checkDuplicate("email", $(this).val(), "#emailCheckMsg");
	});
	$("#nickname").on("blur", function () {
		checkDuplicate("nickname", $(this).val(), "#nickNameCheckMsg");
	});
	// 4. 전화번호 자동 하이픈 및 입력 제한
	// 페이지 로드시 메시지 숨김
	$("#phoneMsg").hide();

	// 입력 중 자동 하이픈 + 최대 13자 제한
	$("#phoneNumber").on("input", function () {
	  let value = $(this).val().replace(/\D/g, "").substring(0, 11); // 숫자만, 최대 11자리
	  let formatted = "";

	  if (value.length >= 10) {
	    formatted = value.replace(/(\d{3})(\d{4})(\d{0,4})/, "$1-$2-$3");
	  } else if (value.length >= 7) {
	    formatted = value.replace(/(\d{3})(\d{0,4})/, "$1-$2");
	  } else {
	    formatted = value;
	  }

	  $(this).val(formatted);
	});

	// 포커스 아웃 시 형식 검사
	$("#phoneNumber").on("blur", function () {
	const pattern = /^(01[0|1|6|7|8|9])-\d{3,4}-\d{4}$/;
	  const $msg = $("#phoneMsg");

	  if (!pattern.test($(this).val())) {
	    $msg.text("형식에 맞지않습니다.").css("color", "red").show();
	  } else {
	    $msg.hide();
	  }
	});

	

	// 5. 회원가입 Ajax 처리
	$(".form-box").on("submit", function (e) {
		e.preventDefault();

		const data = {
			id: $("#userid").val(),
			pw: $("#password").val(),
			email: $("#email").val(),
			nickName: $("#nickname").val(),
			name: $("#name").val(),
			phoneNumber: $("#phoneNumber").val()
		};

		$.ajax({
			type: "POST",
			url: "/CampingLog/controller?cmd=signUp",
			data: data,
			dataType: "json",
			success: function (response) {
				if (response.result === "success") {
					window.location.href = "/CampingLog/controller?cmd=mainUI";
				} else {
					alert("회원가입 실패: 다시 시도해주세요.");
				}
			},
			error: function () {
				alert("서버 연결 실패");
			}
		});
	});
});
