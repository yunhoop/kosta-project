$(function () {
  const urlParams = new URLSearchParams(window.location.search);
  const postId = urlParams.get("postId");
  let memberId = null;

  // (1) 페이지 로드시 세션에서 memberId 가져오기
  $.ajax({
    url: "controller",
    method: "GET",
    data: { cmd: "getSessionMember" },
    dataType: "json",
    success: function (data) {
      memberId = data.memberId;
      console.log("세션에서 가져온 memberId:", memberId);

      // 좋아요 버튼에 data-memberid 속성 세팅
      $(".like-btn").attr("data-memberid", memberId); // ← 요거 꼭 넣어줘야 해
    },
    error: function () {
      console.error("로그인 정보를 가져오는 데 실패했습니다.");
    }
  });

  // (2) 좋아요 버튼 클릭 시
  $(document).on("click", ".like-btn", function () {
    const clickedMemberId = $(this).data("memberid"); // 클릭할 때 읽어오기
    console.log("버튼에서 가져온 memberId:", clickedMemberId);

    if (!postId || !clickedMemberId) {
      alert("⚠️ postId 또는 memberId가 없습니다.");
      return;
    }

    $.ajax({
      url: "controller",
      method: "POST",
      data: {
        cmd: "addHeartAction",
        postId: postId,
        memberId: clickedMemberId
      },
      dataType: "json",
      success: function (responseText) {
        console.log("responseText 전체:", responseText);

        if (responseText.status === true || responseText.status === "true") {
          alert("❤️ 좋아요 성공");
          const current = parseInt($(".like-count").text()) || 0;
          $(".like-count").text(current + 1);
        } else {
          alert("이미 좋아요를 누르셨습니다.");
        }
      },
      error: function () {
        alert("❌ 서버 통신 중 오류 발생");
      }
    });
  });
});
