console.log('boardDetail.js 진입');
import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.8/+esm';
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

const commentsPerPage = 5;

let currentCommentPage = 1;

// DOM 준비되면 시작
waitForDOM('.board-title', init);

function waitForDOM(selector, callback) {
    const el = document.querySelector(selector);
    if (el) return callback();
    setTimeout(() => waitForDOM(selector, callback), 50);
}

function init() {
    const commentList = document.querySelector('.comment-list');
    const pagination = document.querySelector('.pagination');
    const commentInput = document.getElementById('comment-input');
    const commentBtn = document.getElementById('comment-btn');
    const elCommentCount = document.querySelector('.comment-count');
    const ownerButtons = document.querySelector('.owner-buttons');
    const editBtn = document.getElementById('editBtn');
    const deleteBtn = document.getElementById('deleteBtn');
    const bookmarkBtn = document.querySelector('.board-footer-set .btn');
    const bookmarkIcon = bookmarkBtn.querySelector('i');

    axios.get(
        `/api/board-service/${boardId}/member/${memberId}`,
        {
            headers: {
                selfitKosta: token
            }
        }
    )
        .then(response => {
            const board           = response.data;
            const isBookmarked    = response.data.myBookmarkCount > 0;

            console.log('글쓴이 Id:', board.memberId, "로그인 memberId", memberId);
            // 소유자 버튼 표시
            if (board.memberId === memberId) {
                ownerButtons.style.display = 'block';
            }

            // 수정 버튼
            editBtn.onclick = () => {
                location.hash = `/board-service/edit?boardId=${boardId}`;
            };

            // 삭제 버튼
            deleteBtn.onclick = async () => {
                if (!confirm('정말 삭제하시겠습니까?')) return;
                try {
                    await axios.delete(
                        `/api/board-service/${boardId}/member/${memberId}`,
                        {
                            headers: {
                                selfitKosta: token ? `Bearer ${token}` : ''
                            }
                        }
                    );
                    alert('삭제되었습니다.');
                    location.hash = `/api/board-service/list/1/${board.categoryName}`;
                } catch (err) {
                    alert('삭제 중 오류 발생');
                    console.error(err);
                }
            };

            // 화면 렌더링
            renderBoardDetail(board);
            fetchAndRenderComments(currentCommentPage);
            initBookmarkButton(bookmarkBtn, bookmarkIcon, boardId, isBookmarked);
        })
        .catch(err => {
            console.error("게시글 불러오기 실패", err);
        });
    if (commentBtn) {
        commentBtn.onclick = () => addComment(commentInput, elCommentCount, commentList, pagination);
    }

    function addComment(input, countEl, listEl, pageEl) {
        const content = input.value.trim();
        if (!content) return alert('댓글을 입력하세요.');
        if (!token) return window.location.href = '/login';

        axios.post(
            `/api/comment-service/board/${boardId}/comment/member/${memberId}`,
            { commentContent: content },
            {
                headers: {
                    selfitKosta: token
                }
            }
        ).then(() => {
            input.value = '';
            currentCommentPage = 1;
            console.log(content);
            fetchAndRenderComments(currentCommentPage);
        }).catch(err => {
            console.error('댓글 등록 실패', err);
            alert('댓글 등록 중 오류 발생');
        });
    }
    function fetchAndRenderComments(page) {
        axios.get(`/api/comment-service/board/${boardId}/comments/${page}`,
            {
            headers: {
                selfitKosta: token
            }
        }
        ).then(res => {
            const comments = res.data;
            const total = comments[0]?.totalCount || 0;

            console.log(comments);
            renderComments(commentList, comments);
            renderCommentPagination(pagination, total);
            elCommentCount.innerHTML = `<i class="bi bi-chat-dots"></i> ${total}`;
        }).catch(err => {
            console.error('댓글 로드 실패', err);
        });
    }

    function renderComments(container, comments) {
        container.innerHTML = '';
        if (!Array.isArray(comments) || comments.length === 0) {
            container.innerHTML = '<div class="no-comments">등록된 댓글이 없습니다.</div>';
            return;
        }

        comments.forEach(comment => {
            const date = comment.commentDate?.split('.')[0].replace('T', ' ') || '';
            const imgSrc = comment.profileImg || '/img/memberImg.png';

            container.innerHTML += `
                <div class="comment">
                    <img src="${imgSrc}" alt="프로필 이미지" class="comment-profile-img">
                    <div class="comment-right">
                        <div class="comment-nickName-date">
                            <div class="comment-nickName">${comment.nickName || ''}</div>
                            <div class="comment-date">${date}</div>
                        </div>
                        <div class="comment-box">
                            <div class="comment-text">${comment.commentContent || ''}</div>
                        </div>
                    </div>
                </div>
            `;
        });
    }

    function renderCommentPagination(container, total) {
        const totalPages = Math.ceil(total / commentsPerPage);
        container.innerHTML = '';

        if (totalPages <= 1) return;

        const goBtn = (label, page) => `<button onclick="goCommentPage(${page})">${label}</button>`;
        if (currentCommentPage > 1) {
            container.innerHTML += goBtn('«', 1) + goBtn('‹', currentCommentPage - 1);
        }

        for (let i = 1; i <= totalPages; i++) {
            container.innerHTML += `<button class="${i === currentCommentPage ? 'current' : ''}" onclick="goCommentPage(${i})">${i}</button>`;
        }

        if (currentCommentPage < totalPages) {
            container.innerHTML += goBtn('›', currentCommentPage + 1) + goBtn('»', totalPages);
        }

        window.goCommentPage = function (page) {
            currentCommentPage = page;
            fetchAndRenderComments(page);
        };
    }

    function renderBoardDetail(board) {
        document.querySelector('.board-category').textContent = board.categoryName || '';
        document.querySelector('.board-title').textContent = board.boardTitle || '';
        document.querySelector('.nickName').textContent = board.nickName || '';
        document.querySelector('.board-body').textContent = board.boardContent || '';

        const date = board.createdDate?.split('.')[0] || '';
        document.querySelector('.board-date').innerHTML = `<i class="bi bi-clock"></i> ${date}`;
        document.querySelector('.view-count').innerHTML = `<i class="bi bi-eye"></i> ${board.viewCount || 0}`;
        document.querySelector('.comment-count').innerHTML = `<i class="bi bi-chat-dots"></i> ${board.commentCount || 0}`;

        const imgEl = document.querySelector('.board-image img');
        if (board.boardImg && board.boardImg !== 'null') {
            imgEl.src = board.boardImg;
        } else {
            imgEl.removeAttribute('src');
        }
    }

    function initBookmarkButton(btn, icon, boardId, isBookmarked) {
        // 초기 아이콘 세팅
        setBookmarkIcon(isBookmarked);

        btn.onclick = async () => {
            if (!token) return window.location.href = '/html/account/login.html';
            try {
                const res = await axios.put(
                    `/api/board-service/bookmark/${boardId}/member/${memberId}`,
                    {}, // body 없으면 빈 객체
                    { headers: { selfitKosta: token.startsWith('Bearer ') ? token : `Bearer ${token}` } }
                );

                // res.data.message 에 'true' 또는 'false' 문자열이 들어 있으니
                // Boolean 으로 변환
                const active = res.data.message === 'true';
                console.log('북마크 토글 결과:', active);
                setBookmarkIcon(active);
            } catch (err) {
                console.error('북마크 실패', err);
                alert('북마크 오류');
            }
        };

        function setBookmarkIcon(active) {
            icon.className = active ? 'bi bi-bookmark-fill' : 'bi bi-bookmark';
            btn.setAttribute('title', active ? '북마크 해제' : '북마크 추가');
        }
    }
}
