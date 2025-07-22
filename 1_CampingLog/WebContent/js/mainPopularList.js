console.log("popularPost.js 로드됨");
$(document).ready(function () {
	$.ajax({
		url: "controller",
		method: "GET",
		data: { cmd: "mainPopularListAction" },
		success: function (responseText) {
			const data = typeof responseText === "string" ? JSON.parse(responseText) : responseText;

			if (data.length >= 1) {
				const post = data[0];
				const imageSrc = post.postImage ? post.postImage : "defaultImage.png";
				let html = ''
					+ '<div class="topCard" data-postid="' + post.postId + '">'
					+ '  <div class="topImg1"><img src="img/' + imageSrc + '" alt="1위"></div>'
					+ '  <div class="campTitleTop" title="' + post.postTitle + '">' + post.postTitle + '</div>'
					+ '  <div class="campData">'
					+ '    <div class="campUser"><p>' + post.nickName + '</p></div>'
					+ '    <div class="loveDate"><p>좋아요</p><p>' + post.likeCount + '</p><p>' + post.postDate + '</p></div>'
					+ '  </div>'
					+ '</div>';
				$(".topLeft").html(html);
				let titleElement = $(".topLeft .campTitleTop");
				let maxLength = 65;
				let text = titleElement.text();
				if (text.length > maxLength) {
				    titleElement.text(text.substring(0, maxLength) + '...');
				}
				$(document).on("click", ".topCard", function () {
					  const postId = $(this).data("postid");
					  location.href = "detailPost.html?postId=" + postId;
					});
			}

			let htmlRight = "";
			for (let i = 1; i < data.length; i++) {
				const post = data[i];
				const imageSrc = post.postImage ? post.postImage : "defaultImage.png";
				htmlRight += ''
					+ '<div class="topCard" data-postid="' + post.postId + '">'
					+ '  <div class="topImg2"><img src="img/' + imageSrc + '" alt="인기"></div>'
					+ ' <div class="campTitleTop" title="' + post.postTitle + '">' + post.postTitle + '</div> '
					+ '  <div class="campData">'
					+ '    <div class="campUser"><p>' + post.nickName + '</p></div>'
					+ '    <div class="loveDate"><p>좋아요</p><p>' + post.likeCount + '</p><p>' + post.postDate + '</p></div>'
					+ '  </div>'
					+ '</div>';
			}
			$(".topRight").html(htmlRight);
			$(".topRight .campTitleTop").each(function () {
			    const maxLength = 14;
			    let text = $(this).text();
			    if (text.length > maxLength) {
			        $(this).text(text.substring(0, maxLength) + '...');
			    }
			});
			$(document).on("click", ".topCard", function () {
				  const postId = $(this).data("postid");
				  location.href = "detailPost.html?postId=" + postId;
				});
		},
		error: function () {
			console.error("인기 게시글 불러오기 실패");
		}
	});
});