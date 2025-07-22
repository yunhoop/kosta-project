console.log("headerCategory.js 로드됨");

$(document).ready(function () {
	function loadHeaderWithNames() {
		$.ajax({
			url: "controller",
			method: "GET",
			data: { cmd: "getCategoryName" },
			success: function (resp) {
				try {
					if (typeof resp === "string") resp = JSON.parse(resp);
					if (!resp.categoryList || !Array.isArray(resp.categoryList)) {
						console.error("categoryList 형식 오류:", resp);
						return;
					}
					renderHeaderList(resp.categoryList);
				} catch (e) {
					console.error("카테고리 JSON 파싱 실패:", e);
				}
			},
			error: function () {
				console.error("페이지 데이터 로드 실패 (ID+Name)");
			}
		});
	}

	function renderHeaderList(list) {
		const $menu = $(".middleMenu").empty();

		$.each(list, function (i, category) {
			const $link = $(`
					<p>
			<a href="javascript:void(0)" class="category-link" data-category-id="${category.categoryId}">
			${category.categoryName}
			</a>
			</p>
			`);
			$menu.append($link);
		});

		$(".category-link")
		.off("click")
		.on("click", function () {
			const id = $(this).data("categoryId");
			if (id === 21) {
				// 카테고리 ID가 1번이면 notice.html 로
				window.location.href = "notice.html";
			} else {
				// 그 외에는 기존 포스트 목록 페이지로
				window.location.href = "controller?cmd=postUI&categoryId=" + id;
			}
		});
	}

	loadHeaderWithNames();
});
