console.log("RankList.js 로드됨");
$(document).ready(function () {
	$.ajax({
		url: "controller",
		method: "GET",
		data: { cmd: "mainRankUpAction" },
		success: function (response) {
			console.log("전체 회원 등급 갱신 완료", response.result);

			$.ajax({
				url: "controller",
				method: "GET",
				data: { cmd: "yesterdayRankListAction" },
				success: function (responseText) {
					const data = typeof responseText === "string" ? JSON.parse(responseText) : responseText;

					let html = "<div class='rankList'>";
                    // rankDate도 같이 받아와서 표시
                    const rankDate = data.rankDate || "기준일 없음";
                    html += "<div class='rankDate'><p>" + rankDate + " 기준</p></div>";					
					

					$.each(data.rankList, function (index, member) {
						html += ''
							+ '<div class="rankCard" data-memberid="' + member.memberId + '">'
							+ '  <div class="rankBadge"><img src="img/' + member.badgeImage + '" alt="' + (index + 1) + '등 뱃지"></div>'
							+ '  <div class="rankInfo">'
							+ '    <p class="rankDetail">' + (index + 1) + '등</p>'
							+ '    <p class="rankNickname">' + member.nickName + '</p>'
							+ '  </div>'
							+ '</div>';
					});

					html += "</div>";
					$(".downRank").html(html);
					$(document).on("click", ".rankCard", function () {
						  const memberId = $(this).data("memberid");
						  location.href = "yourPage.html?memberId=" + memberId;
					});
				},
				error: function () {
					console.error("명예의 캠핑로거 데이터 불러오기 실패");
				}
			});
		},
		error: function () {
			console.error("전체 회원 등급 갱신 실패");
		}
	});
});
