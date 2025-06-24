console.log('boardDetail.js 진입');
import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.8/+esm';
axios.defaults.baseURL = 'http://54.180.249.146:8881';
axios.defaults.headers.common['Content-Type'] = 'application/json';

const token = localStorage.getItem('auth');
const hashParams = new URLSearchParams(window.location.hash.split('?')[1]);
const boardId = parseInt(hashParams.get('boardId'), 10);
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
        `/api/board/${boardId}`,
        {
            headers: {
                selfitKosta: token ? `Bearer ${token}` : ''
            }
        }
    )
        .then(response => {
            const board           = response.data.board;
            const currentUserId   = response.data.currentUserId;
            const isBookmarked    = response.data.myBookmarkCount > 0;

            // 소유자 버튼 표시
            if (board.memberId === currentUserId) {
                ownerButtons.style.display = 'block';
            }

            // 수정 버튼
            editBtn.onclick = () => {
                location.hash = `/board/edit?boardId=${boardId}`;
            };

            // 삭제 버튼
            deleteBtn.onclick = async () => {
                if (!confirm('정말 삭제하시겠습니까?')) return;
                try {
                    await axios.delete(
                        `/api/board/delete/${boardId}`,
                        {
                            headers: {
                                selfitKosta: token ? `Bearer ${token}` : ''
                            }
                        }
                    );
                    alert('삭제되었습니다.');
                    location.hash = `/board/list?categoryId=${board.categoryId}`;
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
            `/api/board/comment/add`,
            { boardId, commentContent: content },
            { headers: { selfitKosta: `Bearer ${token}` } }
        ).then(() => {
            input.value = '';
            currentCommentPage = 1;
            fetchAndRenderComments(currentCommentPage);
        }).catch(err => {
            console.error('댓글 등록 실패', err);
            alert('댓글 등록 중 오류 발생');
        });
    }

    function fetchAndRenderComments(page) {
        axios.get('/api/board/comments', {
            params: { boardId, page }
        }).then(res => {
            const comments = res.data;
            const total = comments[0]?.totalCount || 0;
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
        setBookmarkIcon(isBookmarked);

        btn.onclick = async () => {
            if (!token) return window.location.href = '/html/account/login.html';
            try {
                const res = await axios.post(`/api/board/bookmark/${boardId}`, {}, {
                    headers: { selfitKosta: `Bearer ${token}` }
                });
                setBookmarkIcon(res.data);
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
