
$(document).ready(() => {
    // 로그인 버튼 클릭 이벤트

    const redirect_url = "http://54.180.249.146:8880/html/dashboard/dashboard.html"
    $("#loginBtn").on("click", () => {
        handleLogin()
    })

    // Enter 키 이벤트 처리
    $("#loginId, #loginPassword").on("keypress", (e) => {
        if (e.which === 13) {
            // Enter 키
            handleLogin()
        }
    })

    // 구글 로그인 버튼 클릭 이벤트
    $("#google-login-btn").on("click", (e) => {
        e.preventDefault()
        openGoogleLoginPopup()
    })

    // 구글 로그인 팝업 열기 함수
    function openGoogleLoginPopup() {
        const authUrl = "http://127.0.0.1:8881/oauth2/authorization/google"
        const popupWidth = 500
        const popupHeight = 600
        const left = (window.innerWidth - popupWidth) / 2
        const top = (window.innerHeight - popupHeight) / 2

        // 팝업 창 열기
        const popup = window.open(
            authUrl,
            "googleLoginPopup",
            `width=${popupWidth},height=${popupHeight},left=${left},top=${top},resizable=yes,scrollbars=yes`,
        )

        window.addEventListener("message", handleAuthMessage)

        setTimeout(() => {
            window.removeEventListener("message", handleAuthMessage)
        }, 300000)

        // 메시지 처리 함수
        function handleAuthMessage(event) {
            // 출처 검증 (필요 시 도메인 추가)
            if (event.origin !== "http://127.0.0.1:8881" && event.origin !== window.location.origin) {
                return;
            }

            if (event.data) {
                if (event.data.token) {
                    // 성공 시 JWT 토큰 저장
                    localStorage.setItem("auth", event.data.token);

                    showAlert("로그인에 성공했습니다.", "success");

                    window.location.href = redirect_url;

                    window.removeEventListener("message", handleAuthMessage);
                } else if (event.data.redirect) {
                    // 실패 시 리다이렉트
                    sessionStorage.setItem("email", event.data.email);
                    sessionStorage.setItem("name", event.data.name);
                    window.location.href = event.data.redirect;

                    window.removeEventListener("message", handleAuthMessage);
                }
            }
        }
    }

    // 로그인 처리 함수
    function handleLogin() {
        const loginId = $("#loginId").val().trim()
        const loginPassword = $("#loginPassword").val().trim()

        // 입력값 검증
        if (!loginId) {
            showAlert("로그인 계정을 입력해주세요.", "warning")
            $("#loginId").focus()
            return
        }

        if (!loginPassword) {
            showAlert("비밀번호를 입력해주세요.", "warning")
            $("#loginPassword").focus()
            return
        }

        // 로딩 상태 표시
        const $loginBtn = $("#loginBtn")
        const originalText = $loginBtn.text()
        $loginBtn.prop("disabled", true).text("로그인 중...")

        // API 요청 데이터
        const loginData = {
            email: loginId,
            pw: loginPassword,
        }

        // Axios 요청 (form-urlencoded 전송)
        axios
            .post("http://54.180.249.146:8881/api/account/login-process", {
                email: loginId,
                pw: loginPassword,
            })
            .then((response) => {
                localStorage.auth = response.headers.selfitkosta
                showAlert("로그인에 성공했습니다.", "success")
                // 필요한 경우 리다이렉션
                window.location.href = redirect_url;
            })
            .catch((error) => {
                let errorMessage = "로그인에 실패했습니다."

                if (error.response) {
                    if (error.response.data && error.response.data.message) {
                        errorMessage = error.response.data.message
                    } else if (error.response.status === 401) {
                        errorMessage = "아이디 또는 비밀번호가 올바르지 않습니다."
                    } else if (error.response.status === 500) {
                        errorMessage = "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
                    }
                }

                showAlert(errorMessage, "error")
            })
            .finally(() => {
                // 로딩 상태 해제
                $loginBtn.prop("disabled", false).text(originalText)
            })
    }

    // 알림 메시지 표시 함수
    function showAlert(message, type = "info") {
        // 기존 알림이 있다면 제거
        $(".custom-alert").remove()

        // 알림 타입에 따른 스타일 클래스
        let alertClass = ""
        let iconClass = ""

        switch (type) {
            case "success":
                alertClass = "alert-success"
                iconClass = "bi-check-circle"
                break
            case "error":
                alertClass = "alert-danger"
                iconClass = "bi-exclamation-circle"
                break
            case "warning":
                alertClass = "alert-warning"
                iconClass = "bi-exclamation-triangle"
                break
            default:
                alertClass = "alert-info"
                iconClass = "bi-info-circle"
        }

        // 알림 HTML 생성
        const alertHtml = `
            <div class="alert ${alertClass} custom-alert" role="alert" style="
                position: fixed;
                top: 20px;
                left: 50%;
                transform: translateX(-50%);
                z-index: 9999;
                min-width: 300px;
                text-align: center;
                border-radius: 8px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                animation: slideDown 0.3s ease-out;
            ">
                <i class="bi ${iconClass} me-2"></i>
                ${message}
            </div>
        `

        // 알림을 body에 추가
        $("body").append(alertHtml)

        // 3초 후 자동 제거
        setTimeout(() => {
            $(".custom-alert").fadeOut(300, function () {
                $(this).remove()
            })
        }, 3000)
    }
})