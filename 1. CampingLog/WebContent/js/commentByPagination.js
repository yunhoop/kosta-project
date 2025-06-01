$(document).ready(function () {
  const urlParams = new URLSearchParams(window.location.search);
  const postId = urlParams.get("postId");
  const pageSize = 5;
  let currentPage = 1;

  function loadComments(page) {
    $.ajax({
      url: "controller",
      method: "GET",
      data: {
        cmd: "commentsByPagination",
        postId: postId,
        page: page
      },
      success: function (responseText) {
        $(".comment-list").empty();

        if (!responseText.commentList || responseText.commentList.length === 0) {
          $(".comment-list").append(`<div class="no-comments">댓글이 없습니다.</div>`);
          $(".comment-count").text(0);
          $(".current-page").text(page);
          renderPagination(1, 1);  // 댓글 없을 때도 페이지 1개 표시
          return;
        }

        responseText.commentList.forEach(function (c) {
          const commentHtml = `
            <div class="comment">
              <div class="profile">
              <img src="img/${c.memberImage || 'defaultMemberImage.png'}" alt="프로필 이미지" class="comment-profile-img">
                <div class="profile-name-badge">
                  <img src="img/${c.badgeImage}" class="comment-badge">
                  <div class="comment-nickName">${c.nickName}</div>
                </div>
              </div>
              <div class="comment-box">
                <div class="comment-text">${c.commentContents}</div>
                <div class="comment-date">${c.commentDate}</div>
              </div>
            </div>
          `;
          $(".comment-list").append(commentHtml);
        });

        $(".comment-count").text(responseText.commentCount || 0);
        $(".current-page").text(responseText.currentPage || 1);

        // 🔥 총 페이지 수 계산하고 페이지네이션 다시 그리기
        const totalPages = Math.ceil((responseText.commentCount || 0) / pageSize);
        renderPagination(totalPages, page);
      },
      error: function () {
        alert("❌ 댓글을 불러오는 중 오류가 발생했습니다.");
      }
    });
  }

  // ⭐ 페이지 번호 그리는 함수
  function renderPagination(totalPages, activePage) {
	const blockSize = 3;
	const currentBlock = Math.floor((activePage - 1) / blockSize);
	  const startPage = currentBlock * blockSize + 1;
	  const endPage = Math.min(startPage + blockSize - 1, totalPages);
    const $pagination = $(".pagination");
    $pagination.empty(); // 기존 것 지우기

    if (startPage > 1) {
        $pagination.append('<div class="arrow">&laquo;</div>');
      }
    
    for (let i = startPage; i <= endPage; i++) {
      if (i === activePage) {
        $pagination.append(`<div class="page current-page">${i}</div>`);
      } else {
        $pagination.append(`<div class="page">${i}</div>`);
      }
    }

    if (endPage < totalPages) {
        $pagination.append('<div class="arrow">&raquo;</div>');
      }
    
  }

  loadComments(currentPage);

  $(document).on("click", ".pagination .page, .pagination .current-page", function () {
    const targetPage = parseInt($(this).text());
    if (!isNaN(targetPage)) {
      currentPage = targetPage;
      loadComments(currentPage);
    }
  });

  $(document).on("click", ".pagination .arrow", function () {
	  const isLeftArrow = $(this).text() === "«";
	  
	  const pageNumbers = $(".pagination .page").map(function () {
	    return parseInt($(this).text());
	  }).get();

	  if (pageNumbers.length === 0) return; // 안전 처리

	  if (isLeftArrow) {
	    // « 클릭: 보이는 번호 중 가장 작은 번호 - 1로 이동
	    const minPage = Math.min(...pageNumbers);
	    if (minPage > 1) {
	      currentPage = minPage - 1;
	      loadComments(currentPage);
	    }
	  } else {
	    // » 클릭: 보이는 번호 중 가장 큰 번호 + 1로 이동
	    const maxPage = Math.max(...pageNumbers);
	    currentPage = maxPage + 1;
	    loadComments(currentPage);
	  }
  });
});
