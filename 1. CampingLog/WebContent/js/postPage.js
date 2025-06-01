// js/postPage.js
console.log("postPage.js 로드됨");

$(document).ready(function () {
  const params     = new URLSearchParams(window.location.search);
  const categoryId = params.get("categoryId");
  if (!categoryId) {
    console.error("categoryId 파라미터가 없습니다!");
    return;
  }
  console.log("읽어온 categoryId:", categoryId);

  const pageSize  = 4;
  const blockSize = 5;
  let totalCount  = 0;

  window.selectedTags = window.selectedTags || [];

  function formatTags(tagListString) {
    if (!tagListString) return "";
    const tags = tagListString.split(",").map(t => t.trim());
    return tags
      .slice(0, 3)
      .map(t => `<span class="tag">#${t}</span>`)
      .join(" ");
  }

  function buildPagination(totalCount, pageSize, blockSize, currentPage) {
    const totalPages   = Math.ceil(totalCount / pageSize);
    const currentBlock = Math.floor((currentPage - 1) / blockSize);
    const startPage    = currentBlock * blockSize + 1;
    const endPage      = Math.min(startPage + blockSize - 1, totalPages);
    let html = "";

    if (currentPage > 1)           html += `<button class="first-page">&laquo;</button>`;
    if (startPage > 1)             html += `<button class="prev-page">&lt;</button>`;

    for (let i = startPage; i <= endPage; i++) {
      const cls = i === currentPage ? "page-number active" : "page-number";
      html += `<button class="${cls}">${i}</button>`;
    }

    if (endPage < totalPages)      html += `<button class="next-page">&gt;</button>`;
    if (currentPage < totalPages)  html += `<button class="last-page">&raquo;</button>`;

    return html;
  }

  function renderSection2(postList, categoryName, currentPage) {
    const $section2 = $("#section2");

    const headerHtml = `
      <div class="section-header">
        <div class="postName"><p>${categoryName || ""}</p></div>
        <div class="write-btn-container">
          <button class="write-btn" onclick="location.href='postWrite.html'">글쓰기</button>
        </div>
      </div>`;

    let contentHtml;
    if (postList.length === 0) {
      contentHtml = `
        <div class="no-post-wrapper">
          <p class="no-post-overlay">게시글이 없습니다.</p>
        </div>`;
    } else {
      const postCards = postList.map(post => {
        const dateStr = post.postDate
          ? new Date(post.postDate).toISOString().split("T")[0]
          : "날짜 없음";

        const imgSrc = post.postImage && post.postImage !== "null"
          ? `/CampingLog/img/${post.postImage}`
          : `/CampingLog/img/default-post.png`;

        return `
          <div class="postCard" data-post-id="${post.postId}" style="cursor:pointer;">
            <div class="postImg">
              <img src="${imgSrc}" alt="게시글 이미지" />
            </div>
            <div class="postContent">
              <div class="postType">
                <div class="categoryName">${post.categoryName || ""}</div>
                <div class="tags">${post.tagList ? formatTags(post.tagList) : ""}</div>
              </div>
              <div class="postTitle">${post.postTitle || ""}</div>
              <div class="postText">${post.postContents || ""}</div>
            </div>
            <div class="postMeta">
              <div class="up">
                <div class="dateData"><p>${dateStr}</p></div>
              </div>
              <div class="postStats">
                <div class="left">
                  <div class="comment"><p>댓글</p></div>
                  <div class="commentData"><p>${post.commentCount ?? 0}</p></div>
                </div>
                <div class="middle">
                  <div class="great"><i class="fa-solid fa-fire"></i></div>
                  <div class="greatData"><p>${post.likeCount ?? 0}</p></div>
                </div>
              </div>
            </div>
          </div>`;
      }).join("");

      contentHtml = `<div class="postCardList">${postCards}</div>`;
    }

    const paginationHtml = buildPagination(totalCount, pageSize, blockSize, currentPage);

    $section2.html(`
      ${headerHtml}
      ${contentHtml}
      <div class="pagination">${paginationHtml}</div>
    `);

    // 상세 페이지 이동 이벤트
    $(".postCard").off("click").on("click", function() {
      const postId = $(this).data("post-id");
      window.location.href = `detailPost.html?postId=${postId}`;
    });
  }

  window.loadPage = function (page) {
    console.log("페이지 불러오기:", page, "tags:", selectedTags);
    $.ajax({
      url: "controller",
      method: "GET",
      data: {
        cmd:        "getPostsByPage",
        page:       page,
        categoryId: categoryId,
        tagList:    selectedTags.join(",")
      },
      success(resp) {
        if (typeof resp === "string") resp = JSON.parse(resp);
        totalCount = resp.totalPostCount;
        const name = resp.postList.length > 0
          ? resp.postList[0].categoryName
          : "";
        renderSection2(resp.postList, name, resp.currentPage);
      },
      error() {
        console.error("게시글 데이터 불러오기 실패");
      }
    });
  };

  $(document).on("click", ".pagination button", function () {
    const btn     = $(this);
    const current = parseInt($(".pagination .active").text()) || 1;
    let selPage;
    if (btn.hasClass("first-page")) selPage = 1;
    else if (btn.hasClass("prev-page")) selPage = current - blockSize;
    else if (btn.hasClass("next-page")) selPage = current + blockSize;
    else if (btn.hasClass("last-page")) selPage = Math.ceil(totalCount / pageSize);
    else selPage = parseInt(btn.text()) || 1;

    selPage = Math.max(1, Math.min(selPage, Math.ceil(totalCount / pageSize)));
    loadPage(selPage);
  });

  // 초기 로딩
  loadPage(1);
});
