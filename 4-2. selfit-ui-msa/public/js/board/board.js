import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.8/+esm';
console.log('board.js 진입');

// axios.defaults.baseURL = 'http://54.180.249.146:8881';
axios.defaults.baseURL = 'http://127.0.0.1:8000';
// axios.defaults.headers.common['Content-Type'] = 'application/json';
// JWT 디코딩 함수
function decodeJwt(token) {
    const jwt = token.startsWith('Bearer ') ? token.substring(7) : token;
    const parts = jwt.split('.');
    if (parts.length !== 3) throw new Error('잘못된 JWT 형식');
    const payloadB64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
    const pad = payloadB64.length % 4;
    const padded = pad ? payloadB64 + '='.repeat(4 - pad) : payloadB64;
    const json = atob(padded);
    return JSON.parse(json);
}

const hashParams = new URLSearchParams(window.location.hash.split('?')[1]);
const boardId = hashParams.get('boardId');
const token   = localStorage.getItem('auth');        // "Bearer eyJh..."

// JWT 페이로드에서 memberId 추출
let memberId;
try {
    const payload = decodeJwt(token);
    memberId = payload.memberId;
} catch (e) {
    console.error('JWT 디코딩 실패', e);
    memberId = null;
}

console.log('boardId :', boardId);
console.log('memberId :', memberId);

(async function initBoard() {
 function getCategoryNameFromHash() {
     const hash = window.location.hash;
     const match = hash.match(/categoryName=([^/]+)/);
     return match
        ? decodeURIComponent(match[1])
         :'';
}
const categoryName  = getCategoryNameFromHash();
 console.log("현재 카테고리 : ", categoryName)

    // — 상태
    let currentPage    = 1;
    let currentSort    = 'asc';
    let currentKeyword = '';
    const pageSize     = 10;

    // — DOM 엘리먼트
    const listContainer = document.querySelector('.board-list');
    const paginationEl  = document.querySelector('.board-pagination');
    const searchInput   = document.querySelector('.searchTotal');
    const sortRadios    = document.querySelectorAll('input[name="sort"]');
    const titleEl       = document.querySelector('.board-title p');
    const writeBtn      = document.getElementById('writeBtn');

    // — 전역 페이지 함수 노출
    window.goPage = page => {
        currentPage = page;
        fetchAndRender();
    };

    // — 이벤트 바인딩
    if (searchInput) {
        searchInput.addEventListener('keydown', e => {
            if (e.key === 'Enter') {
                e.preventDefault();
                currentKeyword = searchInput.value.trim();
                currentPage = 1;
                fetchAndRender();
            }
        });
    }
    sortRadios.forEach(radio => {
        radio.addEventListener('change', () => {
            currentSort = (radio.value === '조회순' ? 'views' : 'asc');
            currentPage = 1;
            fetchAndRender();
        });
    });
    if (writeBtn) {
        writeBtn.addEventListener('click', () => {
            window.location.hash = `/board-service/write?categoryName=${encodeURIComponent(categoryName)}`;
        });
    }

    // — 데이터 가져와 렌더링
    async function fetchAndRender() {
        try {
            const res = await axios.get('/api/board-service/list', {
                params: {
                    page: currentPage,
                    categoryName,
                    sortOrder: currentSort,
                    keyword: currentKeyword
                },

            });
            console.log('fetch URL:', res.config.url);
            const boards    = res.data;
            const totalItems = boards.length ? boards[0].totalCount : 0;

            // 제목 업데이트
            if (boards.length && titleEl) {
                titleEl.textContent = boards[0].categoryName;
            }

            console.log(boards);
            renderList(boards);
            renderPagination(totalItems);
        } catch (err) {
            console.error('게시글 로드 실패', err);
            if (listContainer) {
                listContainer.innerHTML = '<p class="text-center">게시글을 불러올 수 없습니다.</p>';
            }
        }
    }

    function renderList(boards) {
        if (!listContainer) return;
        listContainer.innerHTML = '';

        if (!boards.length) {
            listContainer.innerHTML = '<p class="no-results-message text-center">등록된 게시글이 없습니다.</p>';
            return;
        }

        boards.forEach(b => {
            const item = document.createElement('div');
            item.className = 'board-item d-flex align-items-center py-2';
            item.innerHTML = `
        <div class="flex-fill">
          <a href="#/board-service/detail?boardId=${b.boardId}&categoryName=${encodeURIComponent(categoryName)}">
            ${b.boardTitle}
          </a>
        </div>
        <div class="mx-3">${b.nickName}</div>
        <div class="mx-3">${b.viewCount}</div>
        <div class="mx-3">${b.createdDate.split('T')[0]}</div>
      `;
            listContainer.appendChild(item);
        });
    }

    function renderPagination(totalItems) {
        if (!paginationEl) return;
        const totalPages = Math.ceil(totalItems / pageSize);
        let html = '';

        if (currentPage > 1) {
            html += `<button onclick="goPage(1)">&laquo;</button>`;
            html += `<button onclick="goPage(${currentPage - 1})">&lt;</button>`;
        }
        for (let i = 1; i <= totalPages; i++) {
            html += `<button class="${i === currentPage ? 'current' : ''}" onclick="goPage(${i})">${i}</button>`;
        }
        if (currentPage < totalPages) {
            html += `<button onclick="goPage(${currentPage + 1})">&gt;</button>`;
            html += `<button onclick="goPage(${totalPages})">&raquo;</button>`;
        }

        paginationEl.innerHTML = html;
    }

    // — 최초 호출
    fetchAndRender();
})();
