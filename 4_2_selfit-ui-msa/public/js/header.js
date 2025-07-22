import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.8/+esm';

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

export async function initHeader() {
    await fetchMemberInfo();
    await initCategoryList();
    openGroupByURL();
    setActiveDashboardItem();
    bindMenuLabelToggle();
    bindAuthButtons();
    bindAllDataLinks();

    console.log('bindMenuLabelToggle:', document.querySelector('.sideBar'));
}

// 로그아웃 기능
function handleLogout() {
    if (confirm('정말 로그아웃 하시겠습니까?')) {
        const logoutBtn = document.querySelector('.logout-btn');
        const memberInfo = document.querySelector('.memberInfo');
        const loginBtn = document.querySelector('.login-btn');

        logoutBtn.textContent = '로그아웃 중...';
        logoutBtn.disabled = true;
        logoutBtn.classList.add('loading');

        localStorage.removeItem('auth');

        memberInfo.style.display = 'none';
        logoutBtn.style.display = 'none';
        loginBtn.style.display = 'block';

        alert('로그아웃되었습니다.');
        window.location.href = '/html/account/login.html';
    }
}

// 로그인 페이지로 이동
function handleLogin() {
    window.location.href = '/html/account/login.html';
}

// 사용자 정보 업데이트
function updateUserInfo(userData) {
    const imgEl  = document.querySelector('.memberImg img');
    const goalEl = document.querySelector('.goal p');
    const nameEl = document.querySelector('.memberName p');

    if (imgEl && userData.profileImg) {
        imgEl.src = userData.profileImg;
        imgEl.alt = `${userData.nickname || ''} 프로필 이미지`;
    }
    if (goalEl) goalEl.textContent = userData.goal || '유지';
    if (nameEl) nameEl.textContent = `하고 싶은 ${userData.nickname || '사용자'}님`;
}

// 회원 정보 가져오기
async function fetchMemberInfo() {
    const memberInfo = document.querySelector('.memberInfo');
    const logoutBtn  = document.querySelector('.logout-btn');
    const loginBtn   = document.querySelector('.login-btn');

    try {
        const response = await axios.get(`http://127.0.0.1:8000/api/member-service/member/${memberId}`, {
             headers: { selfitKosta: token.startsWith('Bearer ') ? token : `Bearer ${token}` }
        });
        updateUserInfo(response.data);

        memberInfo.style.display = 'flex';
        logoutBtn.style.display  = 'block';
        loginBtn.style.display   = 'none';
    } catch {
        memberInfo.style.display = 'none';
        logoutBtn.style.display  = 'none';
        loginBtn.style.display   = 'block';
    }
}

function bindAllDataLinks() {
    // data-href 가 붙은 모든 요소에 click 이벤트를 걸자
    document.querySelectorAll('[data-href]').forEach(el => {
        el.style.cursor = 'pointer';
        el.addEventListener('click', () => {
            // SPA 내에서 community 쪽은 해시만 바꾸고
            if (el.closest('[data-group="community"]')) {
                const h = el.getAttribute('data-href').match(/categoryName=\d+/)[0];
                location.hash = `/board-service/list?${h}`;
            }
            // 그 외에는 그냥 href 로 이동
            else {
                window.location.href = el.getAttribute('data-href');
            }
        });
    });
}

// URL 기반 메뉴 그룹 열기
function openGroupByURL() {
    const path = window.location.pathname + window.location.hash;
    document.querySelectorAll('.menu-item.has-children').forEach(item => {
        const grp = item.dataset.group;
        const sub = item.querySelector('.submenu');
        if ((grp === 'community' && path.includes('/board-service/')) ||
            (grp === 'dashboard' && path.startsWith('/dashboard/'))) {
            item.classList.add('open','active');
            if (sub) sub.style.maxHeight = sub.scrollHeight + 'px';
        } else {
            item.classList.remove('open','active');
            if (sub) sub.style.maxHeight = null;
        }
    });
}

// Dashboard 서브메뉴 active
function setActiveDashboardItem() {
    const path = window.location.pathname;
    document.querySelectorAll('[data-group="dashboard"] .submenu-item').forEach(div => {
        const a = div.querySelector('a');
        if (!a) return;
        div.classList.toggle('active', path.startsWith(a.getAttribute('href')));
    });
}

const CATEGORIES = [
    { categoryName: '식단' },
    { categoryName: '운동' },
    { categoryName: '자유' },
];

// Community 카테고리 목록
function initCategoryList() {
    const menu = document.querySelector(
        '.sideBar .menu-item.has-children[data-group="community"] > .submenu'
    );
    if (!menu) return;
    menu.innerHTML = '';
    console.log(CATEGORIES);

    CATEGORIES.forEach(cat => {
        const d = document.createElement('div');
        d.className = 'submenu-item';
        d.textContent = cat.categoryName;

        d.addEventListener('click', () => {
            const isStaticDashboard = window.location.pathname.includes('/dashboard/');
            if (isStaticDashboard) {
                // 일반 HTML 대시보드에서 SPA로 강제 이동
                window.location.href = `/html/spa.html#/board-service/list/categoryName=${cat.categoryName}`;
            } else {
                // SPA 내에서 해시만 바꿔서 뷰 전환
                location.hash = `/board-service/list/categoryName=${cat.categoryName}`;
            }
        });
        menu.appendChild(d);
    });
}

// 메뉴 레이블 토글
function bindMenuLabelToggle() {
    const sidebar = document.querySelector('.sideBar');
    sidebar?.addEventListener('click', e => {
        const label = e.target.closest('.menu-item.has-children .menu-label');
        if (!label) return;

        const item = label.closest('.menu-item.has-children');
        const sub  = item.querySelector('.submenu');

        // 다른 열린 메뉴 닫기
        document.querySelectorAll('.menu-item.has-children').forEach(other => {
            if (other !== item) {
                other.classList.remove('open','active');
                other.querySelector('.submenu').style.maxHeight = null;
            }
        });

        // 클릭한 메뉴 토글
        const isOpen = item.classList.toggle('open');
        item.classList.toggle('active', isOpen);
        sub.style.maxHeight = isOpen ? sub.scrollHeight + 'px' : null;
    });
}

// 로그인/로그아웃 버튼 바인딩
function bindAuthButtons() {
    document.querySelector('.logout-btn')?.addEventListener('click', handleLogout);
    document.querySelector('.login-btn')?.addEventListener('click', handleLogin);
}
