// /js/board/boardForm.js
import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.8/+esm';
import { getDownloadURL, ref, uploadBytes } from 'https://www.gstatic.com/firebasejs/11.8.1/firebase-storage.js';
import { storage } from './firebaseConfig.js';

console.log('boardForm.js 진입');
axios.defaults.headers.common['Content-Type'] = 'application/json';
const token = localStorage.getItem('auth');
// 카테고리 셀렉트 채우기
async function populateCategorySelect(selectedId = null) {
    try {
        const res = await axios.get('http://54.180.249.146:8881/api/category');
        const list = res.data;
        console.log('카테고리 목록:', list);
        const select = document.getElementById('categorySelect');
        select.innerHTML = '<option value="">카테고리 선택</option>';
        list.forEach(cat => {
            const opt = document.createElement('option');
            opt.value = cat.categoryId;
            opt.textContent = cat.categoryName;
            if (selectedId !== null && cat.categoryId === selectedId) {
                opt.selected = true;
            }
            select.appendChild(opt);
        });
    } catch (err) {
        console.error('카테고리 불러오기 실패', err);
    }
}

// 해시에서 path, query 파라미터 파싱
function parseHashParams() {
    const [path, qs] = window.location.hash.slice(1).split('?');
    return { path, params: new URLSearchParams(qs || '') };
}

// 즉시실행 초기화 함수
(async function initBoardForm() {
    const { path, params } = parseHashParams();
    const categoryParam = parseInt(params.get('categoryId'), 10) || null;
    const boardId       = parseInt(params.get('boardId'),    10) || null;
    const isEditMode    = path.startsWith('/board/edit');

    // DOM 요소
    const formTitle       = document.getElementById('form-title');
    const postForm        = document.getElementById('postForm');
    const titleInput      = document.getElementById('boardTitle');
    const categorySelect  = document.getElementById('categorySelect');
    const contentTextarea = document.getElementById('boardContent');
    const imageInput      = document.getElementById('imageFileInput');
    const previewImg      = document.getElementById('previewImg');
    const submitBtn       = document.getElementById('submitBtn');

    // 이미지 업로드/미리보기
    window.triggerImageUpload = () => imageInput.click();
    window.previewImage = inputEl => {
        const file = inputEl.files[0];
        if (!file) return;
        const reader = new FileReader();
        reader.onload = e => previewImg.src = e.target.result;
        reader.readAsDataURL(file);
    };

    // 1) 카테고리 셀렉트 채우기 (쓰기 모드는 URL 파라미터, 수정 모드는 나중에 로드된 값)
    await populateCategorySelect(isEditMode ? null : categoryParam);

    if (isEditMode && boardId) {
        // ── 수정 모드 ──
        formTitle.textContent  = '글 수정';
        submitBtn.textContent  = '수정하기';

        // 기존 글 불러오기
        try {
            const res   = await axios.get(`http://54.180.249.146:8881/api/board/${boardId}`, {
                headers: { selfitKosta: token ? `Bearer ${token}` : '' }
            });
            const b   = res.data.board;
            titleInput.value      = b.boardTitle  || '';
            contentTextarea.value = b.boardContent || '';
            // 카테고리 기본값 적용
            if (b.boardImg && b.boardImg !== 'null') {
                previewImg.src = b.boardImg;
            }

            await populateCategorySelect(b.categoryId);
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
                categoryId:   parseInt(categorySelect.value, 10),
                boardImg:     imageUrl
            };

            try {
                await axios.put(`http://54.180.249.146:8881/api/board/edit/${boardId}`, payload, {
                    headers: { selfitKosta: `Bearer ${token}` }
                });
                alert('글이 수정되었습니다.');
                location.hash = `/board/detail?boardId=${boardId}`;
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
                categoryId:   parseInt(categorySelect.value, 10),
                boardImg:     imageUrl
            };

            try {
                await axios.post('http://54.180.249.146:8881/api/board/add', payload, {
                    headers: { selfitKosta: `Bearer ${token}` }
                });
                alert('게시글 등록 성공');
                location.hash = `/board/list?categoryId=${categorySelect.value}`;
            } catch (err) {
                console.error('등록 실패', err);
                alert('새 글 등록에 실패했습니다.');
            }
        });
    }
})();
