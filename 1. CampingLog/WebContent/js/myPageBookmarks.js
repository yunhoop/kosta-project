console.log("▶ myPageBookmarks.js 로드됨");

$(function(){
  // 북마크 컨테이너가 생성될 때까지 재귀 호출로 대기
  function initWhenReady() {
    const $bookmarks = $("#my-bookmarks");
    if ($bookmarks.length) {
      console.log("▶ 북마크 컨테이너 발견! 개수:", $bookmarks.length);

      // 북마크 목록 로드
      loadBookmarks();

    } else {
      // 아직 컨테이너가 없으면 100ms 후 다시 시도
      setTimeout(initWhenReady, 100);
    }
  }

  initWhenReady();
});

function loadBookmarks() {
  console.log("▶ loadBookmarks 호출");
  $.getJSON("/CampingLog/controller?cmd=myPageBookmarks")
    .done(function(data) {
      console.log("▶ myPageBookmarks 데이터:", data);

      if (data.error === 'unauthorized') {
        alert('로그인이 필요합니다.');
        return location.href = 'login.html';
      }

      const $container = $("#bookmarks .post-list");
      if (!data.length) {
        $container.html("<p>북마크한 글이 없습니다.</p>");
        return;
      }

      let html = '<div class="posts-grid">';
      data.forEach(item => {
        html += `
          <div class="card">
            <img src="img/${item.postImage}" alt="${item.postTitle}">
            <p class="title">${item.postTitle}</p>
          </div>`;
      });
      html += '</div>';

      $container.html(html);
    })
    .fail(function(_, status, err) {
      console.error("▶ myPageBookmarks AJAX 실패:", status, err);
      $("#bookmarks .post-list")
        .html("<p>데이터 로드 중 오류가 발생했습니다.</p>");
    });
}
