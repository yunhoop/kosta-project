$(function () {
	const ctx = '/CampingLog';

	// 1) 프로필 정보 불러오기
	$.getJSON(`${ctx}/controller?cmd=getMyInfo`)
	.done(function(data) {
		// (1) 로그인 체크
		if (data.error === 'unauthorized') {
			alert('로그인이 필요합니다.');
			return location.href = `${ctx}/controller?cmd=login`;
		}

		// (2) 프로필 이미지
		$('#profileImage')
		.attr('src', data.memberImage
				? `${ctx}/img/${data.memberImage}`
						: `${ctx}/img/default.jpg`)
						.attr('alt', '프로필 이미지');

		// (3) 요약 아이콘
		$('.summary-icon')
		.attr('src', data.badgeImage
				? `${ctx}/img/${data.badgeImage}`
						: `${ctx}/img/defaultBadge.png`)
						.attr('alt', '뱃지 아이콘');

		// (4) 활동 요약 텍스트
		$('.summary-text').html(`
				<p><strong>내 활동 요약</strong></p>
				<p>작성한 글 : ${data.postCount}개</p>
				<p>받은 좋아요 : ${data.likeCount}개</p>
				<p>가입 일자 : ${data.inDate.split(' ')[0]}</p>
				`);

		// (5) 기본 정보
		$('.profile-details').html(`
				<p><strong>아이디</strong> ${data.memberId}</p>
				<p><strong>이메일</strong> ${data.email}</p>
				<p><strong>닉네임</strong> ${data.nickName}</p>
				<p><strong>이름</strong> ${data.name}</p>
				<p><strong>전화번호</strong> ${data.phoneNumber}</p>
				`);
	})
	.fail(function() {
		alert('서버에 문제가 발생했습니다. 다시 시도해주세요.');
		location.href = `${ctx}/login.html`;
	});


	// 2) 탭 전환
	$('#viewSelector').on('change', function () {
		$('.post-list-section').hide();
		$(`#${this.value}`).show();
	});


	// 3) 게시글/북마크 로딩 함수
	function loadSection(cmd, containerId, emptyMsg) {
		$.getJSON(`${ctx}/controller?cmd=${cmd}`)
		.done(function(items) {
			// unauthorized 체크
			if (items.error === 'unauthorized') {
				alert('로그인이 필요합니다.');
				return location.href = `${ctx}/controller?cmd=login`;
			}

			const $list = $(`#${containerId} .post-list`);
			if (!items || items.length === 0) {
				$list.html(`<p>${emptyMsg}</p>`);
			} else {
				$list.html(items.map(item => `
						<div class="post-item">
				<img src="${ctx}/img/${item.postImage}" alt="${item.postTitle}">
				<p class="post-title">${item.postTitle}</p>
				</div>
				`).join(''));
			}
			// 보이기
			$(`#${containerId}`).show();
		})
		.fail(function() {
			$(`#${containerId} .post-list`)
			.html('<p>데이터 로드 중 오류가 발생했습니다.</p>');
		});
	}

	// 4) 초기 로드
	loadSection('myPagePosts',     'my-posts',     '작성한 글이 없습니다.');
	loadSection('myPageBookmarks', 'my-bookmarks', '북마크한 글이 없습니다.');
	// 5) 정보 수정 버튼 클릭 → 비밀번호 확인 페이지로 이동
	$(document).on('click', '.edit-btn', function () {
	  window.location.href = 'passwordCheck.html?target=edit';
	});

	// 6) 회원 탈퇴 버튼 클릭 → 비밀번호 확인 페이지로 이동
	$(document).on('click', '.delete-btn', function () {
	  window.location.href = 'passwordCheck.html?target=delete';
	console.log("✅ mypage.js 실행됨");
	});
});
