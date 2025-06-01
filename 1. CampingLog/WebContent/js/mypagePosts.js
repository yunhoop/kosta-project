console.log("▶ mypagePosts.js 로드됨");

$(function(){
  // 컨테이너가 생성될 때까지 재귀 호출로 대기
  function initWhenReady() {
    const $posts = $("#my-posts");
    const $bookmarks = $("#my-bookmarks");
    // 둘 다 생성된 순간 초기화
    if ($posts.length && $bookmarks.length) {
      console.log("▶ 컨테이너 발견! 포스트 개수:", $posts.length);
      console.log("▶ 컨테이너 발견! 북마크 개수:", $bookmarks.length);

      // 1) 두 목록 로드
      loadList('myPagePosts',     '#my-posts',     '작성한 글이 없습니다.');
      loadList('myPageBookmarks', '#my-bookmarks', '북마크한 글이 없습니다.');

      // 2) 뷰 셀렉터 토글
      $('#viewSelector').on('change', function(){
        if (this.value === 'my-posts') {
          $('#my-posts').show();
          $('#my-bookmarks').hide();
        } else {
          $('#my-posts').hide();
          $('#my-bookmarks').show();
        }
      });

    } else {
      // 아직 컨테이너가 없으면 100ms 후 다시 시도
      setTimeout(initWhenReady, 100);
    }
  }

  initWhenReady();
});

function loadList(cmd, container, emptyText) {
  console.log(`▶ loadList 호출: ${cmd}`);
  $.getJSON(`/CampingLog/controller?cmd=${cmd}`)
    .done(function(data) {
      console.log(`▶ ${cmd} 데이터:`, data);
      if (data.error === 'unauthorized') {
        alert('로그인이 필요합니다.');
        return location.href = 'login.html';
      }
      const $c = $(container);
      if (!data.length) {
        $c.html(`<p>${emptyText}</p>`);
        return;
      }
      let html = '<div class="posts-grid">';
      data.slice(0, 3).forEach(item => {
        html += `
          <div class="card">
            <img src="img/${item.postImage}" alt="${item.postTitle}">
            <p class="title">${item.postTitle}</p>
          </div>`;
      });
      html += '</div>';
      $c.html(html);
    })
    .fail(function(_, status, err) {
      console.error(`▶ ${cmd} AJAX 실패:`, status, err);
      $(container).html('<p>데이터 로드 중 오류가 발생했습니다.</p>');
    });
}
