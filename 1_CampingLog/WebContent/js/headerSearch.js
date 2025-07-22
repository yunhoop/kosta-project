console.log("headerSearch.js 로드됨");

$(document).ready(function(){
	const pageSize  = 4;
	const blockSize = 5;

	let totalCount   = 0;
	let currentPage  = 1;
	let currentQuery = "";

	// ── 0) 초기 URL 파라미터에서 cmd, searchTerm, page 읽어서 자동 로드 ──
	const params   = new URLSearchParams(window.location.search);
	const initCmd  = params.get("cmd");
	const initTerm = params.get("searchTerm");
	const initPage = parseInt(params.get("page"), 10);

	if (initCmd === "searchResultUI" && initTerm) {
		currentQuery = initTerm;
		// 검색창에도 값 채워주기
		$(".searchBox").find('input[name="searchTerm"]').val(initTerm);
		// 지정된 페이지(없으면 1) 로드
		loadPage(initPage > 0 ? initPage : 1);
	}

	// ── 페이지 버튼 마크업 생성 ──
	function buildPagination(total, pageSize, blockSize, curPage) {
		const totalPages   = Math.ceil(total / pageSize);
		const currentBlock = Math.floor((curPage - 1) / blockSize);
		const startPage    = currentBlock * blockSize + 1;
		const endPage      = Math.min(startPage + blockSize - 1, totalPages);
		let html = "";

		if (curPage > 1)           html += '<button class="first-page">&laquo;</button>';
		if (startPage > 1)         html += '<button class="prev-page">&lt;</button>';
		for (let i = startPage; i <= endPage; i++) {
			const cls = i === curPage ? "page-number active" : "page-number";
			html += `<button class="${cls}">${i}</button>`;
		}
		if (endPage < totalPages)  html += '<button class="next-page">&gt;</button>';
		if (curPage < totalPages)  html += '<button class="last-page">&raquo;</button>';
		return html;
	}

	// ── 태그 마크업 (#태그 형식) ──
	function formatTags(tagListString) {
		if (!tagListString) return "";
		return tagListString
		.split(",")
		.map(t => t.trim())
		.slice(0, 3)
		.map(t => `<span class="tag">#${t}</span>`)
		.join(" ");
	}

	// ── 결과를 화면에 렌더링 ──
	function renderResults(list, total, page) {
		totalCount  = total;
		currentPage = page;

		const $list = $(".postCardList");
		const $pag  = $(".pagination");
		$list.empty();
		$pag.empty();

		if (!list.length) {
			$list.html(`
					<div class="no-post-wrapper">
			<p class="no-post-overlay">검색 결과가 없습니다.</p>
			</div>
			`);
			return;
		}

		// 게시물 카드 생성
		list.forEach(post => {
			const dateStr = post.postDate
			? new Date(post.postDate).toISOString().split("T")[0]
			: "날짜 없음";
			const imgSrc = (post.postImage && post.postImage !== "null")
			? post.postImage
					: "img/postImg1.png";

			$list.append(`
					<div class="postCard">
			<div class="postImg">
			<img src="${imgSrc}" alt="게시글 이미지" />
				</div>
			<div class="postContent">
			<div class="postType">
			<div class="categoryName">${post.categoryName}</div>
			<div class="tags">${formatTags(post.tagList)}</div>
			</div>
			<div class="postTitle">${post.postTitle}</div>
			<div class="postText">${post.postContents}</div>
			</div>
			<div class="postMeta">
			<div class="up">
			<div class="dateData"><p>${dateStr}</p></div>
			</div>
			<div class="postStats">
			<div class="left">
			<div class="comment"><p>댓글</p></div>
			<div class="commentData"><p>${post.commentCount||0}</p></div>
			</div>
			<div class="middle">
			<div class="great"><i class="fa-solid fa-fire"></i></div>
			<div class="greatData"><p>${post.likeCount||0}</p></div>
			</div>
			</div>
			</div>
			</div>
			`);
		});

		// 페이징 버튼 삽입
		$pag.html(buildPagination(totalCount, pageSize, blockSize, currentPage));
	}

	// ── 서버에서 검색 결과 가져오기 ──
	function loadPage(page) {
		$.ajax({
			url: "controller",
			method: "GET",
			dataType: "json",
			data: {
				cmd:        "searchResultPage",
				searchTerm: currentQuery,
				page:       page
			},
			success(resp) {
				if (typeof resp === "string") resp = JSON.parse(resp);
				renderResults(resp.postList, resp.totalPostCount, resp.currentPage);
			},
			error(err) {
				console.error("검색 실패", err);
				$(".postCardList").html("<p>검색 중 오류가 발생했습니다.</p>");
			}
		});
	}

	// ── 헤더 검색 폼(submit) 가로채기 ──
	$(".searchBox").on("submit", function(e){
		e.preventDefault();
		const term = $(this).find('input[name="searchTerm"]').val().trim();
		if (!term) return;

		currentQuery = term;
		console.log(currentQuery);
		// URL 히스토리 교체 (optional)
		window.history.pushState({}, "", `?cmd=searchResultUI&searchTerm=${encodeURIComponent(term)}`);
		loadPage(1);
	});

	// ── 페이징 버튼 클릭 처리 ──
	$(document).on("click", ".pagination button", function(){
		const btn    = $(this);
		const active = parseInt($(".pagination .active").text(), 10) || 1;
		let selPage;

		if (btn.hasClass("first-page"))    selPage = 1;
		else if (btn.hasClass("prev-page")) selPage = active - blockSize;
		else if (btn.hasClass("next-page")) selPage = active + blockSize;
		else if (btn.hasClass("last-page")) selPage = Math.ceil(totalCount / pageSize);
		else selPage = parseInt(btn.text(), 10) || 1;

		selPage = Math.max(1, Math.min(selPage, Math.ceil(totalCount / pageSize)));
		loadPage(selPage);
	});

});
