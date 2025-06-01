$(function () {
  $(".bookmark-btn").click(function () {  // ⭐ 북마크 버튼 id 맞춰야 해!
    const postId = new URLSearchParams(window.location.search).get("postId");

    if (!postId) {
      alert("postId가 없습니다.");
      return;
    }

    // (1) 세션에서 memberId 먼저 가져오기
    $.ajax({
      url: "controller",
      method: "GET",
      data: { cmd: "getSessionMember" },
      dataType: "json",
      success: function (data) {
        const memberId = data.memberId;

        if (!memberId) {
          alert("로그인이 필요합니다.");
          return;
        }

        // (2) 가져온 memberId로 북마크 등록
        $.ajax({
          url: "controller?cmd=addBookmark",
          method: "POST",
          data: {
            postId: postId,
            memberId: memberId
          },
          success: function (data) {
            if (data.status === "true") {
              alert("⭐ 북마크 완료!");
              const current = parseInt($(".bookmark-count").text()) || 0;
              $(".bookmark-count").text(current + 1);
            } else {
              alert("이미 북마크했습니다.");
            }
          },
          error: function () {
            alert("북마크 등록 실패");
          }
        });
      },
      error: function () {
        alert("로그인 정보를 가져오는 데 실패했습니다.");
      }
    });
  });
});
