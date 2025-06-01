console.log("📄 detail.js 로드됨");

$(document).ready(function () {
	$(".edit-btn, #delete-btn").hide(); // 기본 숨김
  const urlParams = new URLSearchParams(window.location.search);
  const postId = urlParams.get("postId");
  $("#postId").val(postId);

  if (!postId) {
    console.error("❌ postId 없음. input[type=hidden] 확인 필요.");
    return;
  }

  let sessionMemberId = null;

  // 🔥 (1) 세션 사용자 정보 먼저 가져오기
  $.ajax({
    url: "controller",
    method: "GET",
    data: { cmd: "getSessionMember" },
    dataType: "json",
    success: function (data) {
      sessionMemberId = data.memberId;
      const sessionUserImage = data.userImage;
      console.log("세션에서 가져온 memberId:", sessionMemberId);
      console.log("세션에서 가져온 userImage:", sessionUserImage);

      // 댓글 입력창 프로필 이미지 설정
      $(".comment-input img.comment-profile-img").attr(
        "src",
        "img/" + (sessionUserImage || "defaultMemberImage.png")
      );

      // 🔥 (2) 게시글 상세 정보 가져오기
      $.ajax({
        url: "controller",
        method: "GET",
        data: { cmd: "detail", postId: postId },
        success: function (responseText) {
          const data = typeof responseText === "string" ? JSON.parse(responseText) : responseText;

          // 게시글 정보 출력
          $(".post-title").text(data.postTitle);
          $(".nickName").text(data.nickName);
          $(".post-info div").text(data.postDate);
          $(".post-image img").attr("src", "img/" + data.postImage);
          $(".like-count").text(data.likeCount);
          $(".bookmark-count").text(data.bookmarkCount);
          $(".post-body").text(data.postContents);
          $(".page-title").text(data.categoryName);
          $(".profile-name-badge img").attr("src", "img/" + data.badgeImage);
          $(".profile-img").attr("src", "img/" + data.memberImage);
          $(".like-btn").attr("data-memberid", sessionMemberId);

          if (data.memberId === sessionMemberId) {
        	  $(".edit-btn, #delete-btn").show();
        	}
          
          // 태그 출력
          let tagHtml = "";
          for (let i = 0; i < data.tagList.length; i++) {
            tagHtml += '<div>#' + data.tagList[i] + '</div>';
          }
          $(".tags").html(tagHtml);

          // 댓글 출력
          let commentHtml = "";
          for (let i = 0; i < data.commentList.length; i++) {
            const c = data.commentList[i];
            commentHtml += ''
              + '<div class="comment">'
              + '  <div class="profile">'
              + '    <img src="img/defaultMemberImage.png" alt="프로필 이미지" onerror="this.src=&#39;img/defaultMemberImage.png&#39;" class="comment-profile-img" data-memberid="' + c.memberId + '">'
              + '    <div class="profile-name-badge">'
              + '      <img class="comment-badge" src="img/' + c.badgeImage + '">'
              + '      <div class="comment-nickName">' + c.nickName + '</div>'
              + '    </div>'
              + '  </div>'
              + '  <div class="comment-box">'
              + '    <div class="comment-text">' + c.commentContents + '</div>'
              + '    <div class="comment-date">' + c.commentDate + '</div>'
              + '  </div>'
              + '</div>';
          }
          $(".pagination").before(commentHtml);

          // 댓글 작성자 이미지 비동기 로딩
          $(".comment-profile-img").each(function () {
            const $img = $(this);
            const memberId = $img.data("memberid");

            if (memberId) {
              $.ajax({
                url: "controller",
                method: "GET",
                data: {
                  cmd: "memberImageUIAction",
                  memberId: memberId
                },
                dataType: "json",
                success: function (response) {
                  if (response.userImage) {
                	  $img.attr("src", "img/" + (response.userImage || "defaultMemberImage.png"));
                  }
                },
                error: function () {
                  $img.attr("src", "img/defaultMemberImage.png");
                }
              });
            }
          });
        },
        error: function () {
          console.error("❌ 게시글 상세 데이터를 불러오는 데 실패했습니다.");
        }
      });
    },
    error: function () {
      console.error("❌ 세션 사용자 정보를 가져오는 데 실패했습니다.");
    }
  });

  // 수정 버튼
  $(document).on("click", ".edit-btn", function () {
    location.href = `postEdit.html?postId=${postId}`;
  });

  // 삭제 버튼
  $(document).on("click", "#delete-btn", function () {
    const confirmDelete = confirm("정말 이 게시글을 삭제하시겠습니까?");
    if (!confirmDelete) return;

    $.ajax({
      url: "controller",
      method: "POST",
      data: {
        cmd: "deletePostAction",
        postId: postId
      },
      dataType: "json",
      success: function (res) {
        if (res.result === "success") {
          alert("게시글이 삭제되었습니다.");
          window.location.href = "mainUI.html";
        } else {
          alert("삭제 실패: " + (res.message || "서버 오류"));
        }
      },
      error: function () {
        alert("서버 오류로 삭제에 실패했습니다.");
      }
    });
  });
});
