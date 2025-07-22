console.log("mainEquList.js 로드됨");

$(document).ready(function () {
  $.ajax({
    url: "controller",
    method: "GET",
    data: { cmd: "mainEquListAction" },
    success: function (responseText) {
      const data = typeof responseText === "string" ? JSON.parse(responseText) : responseText;

      let html = "";
      for (let i = 0; i < data.length; i++) {
        const post = data[i];
        html += ''
          + '<swiper-slide>' //swiper-slide로 감싸기 시작
          + '<div class="downCard" data-postid="' + post.postId + '">'
          + '    <div class="downtop">'
          + '  <div class="campTitleTop" title="' + post.postTitle + '">' + post.postTitle + '</div>'
          + '      <div class="downImg"><img src="img/' + post.postImage + '" alt="장비리뷰' + (i + 1) + '"></div>'
          + '    </div>'
          + '    <div class="campData">'
          + '      <div class="campUser"><p>' + post.nickName + '</p></div>'
          + '      <div class="loveDate"><p>좋아요</p><p>' + post.likeCount + '</p><p>' + post.postDate + '</p></div>'
          + '    </div>'
          + '  </div>'
          + '</swiper-slide>'; //swiper-slide 끝
      }

      // downList 안의 swiper-container에 슬라이드 넣기
      $(".downList").find("swiper-container").html(html);
      const swiperEl = document.querySelector(".downList swiper-container");
      if (swiperEl) {
        swiperEl.removeAttribute("navigation"); // 혹시 중복 방지
      }
      $(".downList .campTitleTop").each(function () {
    	    const maxLength = 12;
    	    let text = $(this).text();
    	    if (text.length > maxLength) {
    	        $(this).text(text.substring(0, maxLength) + '...');
    	    }
      });
      $(document).on("click", ".downCard", function () {
          const postId = $(this).data("postid");
          location.href = "detailPost.html?postId=" + postId;
      });
    },
    error: function () {
      console.error("장비 리뷰 불러오기 실패");
    }
  });
});
