
/**
 * data-include로 지정된 모든 HTML 조각을 fetch → innerHTML 삽입
 */
async function includeHTML() {
	const targets = document.querySelectorAll('[data-include]');
	for (const el of targets) {
		const file = el.dataset.include;
		try {
			const res = await fetch(file);
			if (!res.ok) throw new Error(`${file} 로딩 실패: ${res.status}`);
			el.innerHTML = await res.text();
		} catch (err) {
			console.error(err);
		}
	}
}

/**
 * 동적 <script> 태그 로드 유틸
 * @param {string} src - 로드할 스크립트 경로
 * @returns {Promise<string>} resolve된 src
 */
function loadScript(src) {
	return new Promise((resolve, reject) => {
		const s = document.createElement('script');
		s.src    = src;
		s.onload = () => resolve(src);
		s.onerror= () => reject(new Error(`스크립트 로드 실패: ${src}`));
		document.body.appendChild(s);
	});
}

async function initPage() {
	// 1) 공통 HTML 조각(include) 삽입
	await includeHTML();

	// 2) header.html을 include한 페이지라면 headerCategory.js 로드
	if (document.querySelector('[data-include="header.html"]')) {
		await loadScript('js/headerCategory.js');
		await loadScript('js/headerSearch.js');
		await loadScript('js/headerAuthUI.js');
	}

	// 3) 파일명으로 페이지 전용 스크립트 분기
	const path = window.location.pathname;
	const page = path.substring(path.lastIndexOf('/') + 1);

	switch (page) {
	case 'mainUI.html':
		await loadScript('js/mainPopularList.js');
		await loadScript('js/mainRankList.js');
		await loadScript('js/mainEquList.js');
		break;

	case 'groundPost.html':
		await loadScript('js/searchTag.js');
		await loadScript('js/postPage.js');
		break;

	case 'detailPost.html':
		await loadScript('js/detail.js');
		await loadScript('js/commentCount.js');
		await loadScript('js/commentByPagination.js');
		await loadScript('js/addComment.js');
		await loadScript('js/addBookmark.js');
		await loadScript('js/addHeart.js');
		break;

	case 'myPage.html':
		await loadScript('js/headerCategory.js');
		await loadScript('js/headerSearch.js');
		await loadScript('js/headerAuthUI.js');
		await loadScript('js/mypage.js');
		await loadScript('js/mypagephoto.js');
		await loadScript('js/mypagePosts.js');
		await loadScript('js/myPageBookmarks.js');
		break;
	case 'yourPage.html':
		// 예: 마이/상대 페이지 전용 스크립트가 있다면 여기서 로드
		break;

		// 로그인·회원가입·비밀번호 확인 등 header 필요 없는 페이지는 여기서 아무 것도 안 함
	default:
		break;
	}

	const cmd = new URLSearchParams(location.search).get('cmd');

	console.log(cmd);

	switch (cmd) {
	case 'postUI':
		await loadScript('js/searchTag.js');
		await loadScript('js/postPage.js');
		break;

	default:
		break;
	}
}

//DOMContentLoaded 시 한 번만 실행
document.addEventListener('DOMContentLoaded', initPage);