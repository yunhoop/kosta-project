// /js/board/boardForm.js
import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.8/+esm';
import { getDownloadURL, ref, uploadBytes } from 'https://www.gstatic.com/firebasejs/11.8.1/firebase-storage.js';
import { storage } from './firebaseConfig.js';
axios.defaults.baseURL = 'http://127.0.0.1:8000';
console.log('boardForm.js 진입');
// axios.defaults.headers.common['Content-Type'] = 'application/json';

const CATEGORIES = ['식단', '운동', '자유'];

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

function populateCategorySelect(selectedCategory = null) {
    const selectEl = document.getElementById('categorySelect');
    selectEl.innerHTML = `<option value="">카테고리를 선택하세요</option>`;
    CATEGORIES.forEach(name => {
        const opt = document.createElement('option');
        opt.value       = name;
        opt.textContent = name;
        if (name === selectedCategory) opt.selected = true;
        selectEl.appendChild(opt);
    });
}

// 해시에서 path, query 파라미터 파싱
function parseHashParams() {
    const [path, qs] = window.location.hash.slice(1).split('?');
    return { path, params: new URLSearchParams(qs || '') };
}

// 즉시실행 초기화 함수
(async function initBoardForm() {
    const { path, params } = parseHashParams();
    const categoryNameParam = params.get('categoryName') || null;
    const boardId           = params.get('boardId')       || null;
    const isEditMode    = path.startsWith('/board-service/edit');

    // DOM 요소
    const formTitle       = document.getElementById('form-title');
    const postForm        = document.getElementById('postForm');
    const titleInput      = document.getElementById('boardTitle');
    const categorySelect  = document.getElementById('categorySelect');
    const contentTextarea = document.getElementById('boardContent');
    const imageInput      = document.getElementById('imageFileInput');
    const previewImg      = document.getElementById('previewImg');
    const submitBtn       = document.getElementById('submitBtn');

    populateCategorySelect(isEditMode ? null : categoryNameParam);
    // 이미지 업로드/미리보기
    window.triggerImageUpload = () => imageInput.click();
    window.previewImage = inputEl => {
        const file = inputEl.files[0];
        if (!file) return;
        const reader = new FileReader();
        reader.onload = e => previewImg.src = e.target.result;
        reader.readAsDataURL(file);
    };


    if (isEditMode && boardId) {
        // ── 수정 모드 ──
        formTitle.textContent  = '글 수정';
        submitBtn.textContent  = '수정하기';

        // 기존 글 불러오기
        try {
            const res   = await axios.get(`/api/board-service/${boardId}`, {
                headers: { selfitKosta: token ? `Bearer ${token}` : '' }
            });
            const b   = res.data.board;
            titleInput.value      = b.boardTitle  || '';
            contentTextarea.value = b.boardContent || '';
            // 카테고리 기본값 적용
            if (b.boardImg && b.boardImg !== 'null') {
                previewImg.src = b.boardImg;
            }

            populateCategorySelect(b.categoryName);
        } catch (err) {
            console.error('수정 대상 게시글 로드 실패', err);
            alert('게시글을 불러오지 못했습니다.');
            return;
        }

        // 수정 제출
        postForm.addEventListener('submit', async e => {
            e.preventDefault();
            if (!titleInput.value.trim())       { alert('제목을 입력하세요.'); return; }
            if (!contentTextarea.value.trim())  { alert('내용을 입력하세요.'); return; }
            if (!categorySelect.value)          { alert('카테고리를 선택하세요.'); return; }

            // 이미지 업로드 처리
            let imageUrl = previewImg.src;
            if (imageInput.files[0]) {
                const file = imageInput.files[0];
                const storageRefObj = ref(storage, `boards/${Date.now()}_${file.name}`);
                try {
                    const snap = await uploadBytes(storageRefObj, file);
                    imageUrl = await getDownloadURL(snap.ref);
                } catch (uploadErr) {
                    console.error('Firebase 업로드 실패', uploadErr);
                    alert('이미지 업로드에 실패했습니다.');
                    return;
                }
            }

            // 페이로드
            const payload = {
                boardTitle:   titleInput.value.trim(),
                boardContent: contentTextarea.value.trim(),
                categoryName:   categorySelect.value,
                boardImg:     imageUrl
            };

            try {
                await axios.put(`/api/board-service/edit/${boardId}`, payload, {
                    headers: { selfitKosta: `Bearer ${token}` }
                });
                alert('글이 수정되었습니다.');
                location.hash = `/board-service/detail/${boardId}/${payload.categoryName}`;
            } catch (err) {
                console.error('수정 중 오류', err);
                alert('글 수정에 실패했습니다.');
            }
        });

    } else {
        // ── 쓰기 모드 ──
        formTitle.textContent = '글 작성';
        submitBtn.textContent = '게시하기';

        postForm.addEventListener('submit', async e => {
            e.preventDefault();
            if (!titleInput.value.trim())       { alert('제목을 입력하세요.'); return; }
            if (!contentTextarea.value.trim())  { alert('내용을 입력하세요.'); return; }
            if (!categorySelect.value)          { alert('카테고리를 선택하세요.'); return; }

            let imageUrl = null;
            if (imageInput.files[0]) {
                const file = imageInput.files[0];
                const storageRefObj = ref(storage, `boards/${Date.now()}_${file.name}`);
                try {
                    const snap = await uploadBytes(storageRefObj, file);
                    imageUrl = await getDownloadURL(snap.ref);
                } catch (uploadErr) {
                    console.error('Firebase 업로드 실패', uploadErr);
                    alert('이미지 업로드에 실패했습니다.');
                    return;
                }
            }

            const payload = {
                boardTitle:   titleInput.value.trim(),
                boardContent: contentTextarea.value.trim(),
                categoryName:   categorySelect.value,
                boardImg:     imageUrl
            };

            try {
                await axios.post(`/api/board-service/member/${memberId}`, payload, {
                    headers: {
                        selfitKosta: token
                    }
                });

                alert('게시글 등록 성공');
                location.hash = `/board-service/list?categoryName=${encodeURIComponent(categorySelect.value)}`;
            } catch (err) {
                console.error('등록 실패', err);
                alert('새 글 등록에 실패했습니다.');
            }
        });
    }
})();
