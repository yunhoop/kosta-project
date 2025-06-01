$(function () {
	$("#comment-btn").click(function () {
		const commentContents = $("#comment-input").val();
		const postId = new URLSearchParams(window.location.search).get("postId");

		if (!commentContents.trim()) {
			alert("댓글을 입력해주세요.");
			return;
		}

		// 1. 먼저 세션에서 memberId 가져오기
		$.ajax({
			url: "controller",
			method: "GET",
			data: { cmd: "getSessionMember" },
			dataType: "json",
			success: function (data) {
				const memberId = data.memberId;  // 서버에서 받아온 memberId 사용

				if (!memberId) {
					alert("로그인이 필요합니다.");
					return;
				}

				// 2. memberId를 가지고 댓글 등록하기
				$.ajax({
					url: `controller?cmd=addComment`,
							method: "POST",
							data: {
								postId: postId,
								commentContents: commentContents,
								memberId: memberId
							},
							success: function (data) {
								alert("댓글이 등록되었습니다.");
								location.reload(); // 댓글 등록 후 페이지 새로고침
							},
							error: function () {
								alert("댓글 등록 실패");
							}
				});
			},
			error: function () {
				alert("로그인 정보를 가져오는 데 실패했습니다.");
			}
		});

	});
});