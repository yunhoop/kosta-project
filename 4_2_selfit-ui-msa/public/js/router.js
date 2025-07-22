import { initLayoutSPA } from '/js/layout-loader.js';

const routes = {
    '/board-service/write': {
        html: '/html/board/boardForm.html',
        css:  '/css/board/boardWrite.css',
        js:   '/js/board/boardForm.js',
    },
    '/board-service/edit': {
        html: '/html/board/boardForm.html',
        css:  '/css/board/boardWrite.css',
        js:   '/js/board/boardForm.js',
    },
    '/board-service/list': {
        html: '/html/board/board.html',
        css:  '/css/board/board.css',
        js:   '/js/board/board.js',
    },
    '/board-service/detail': {
        html: '/html/board/boardDetail.html',
        css:  '/css/board/boardDetail.css',
        js:   '/js/board/boardDetail.js',
    },
};

function parsePath() {
    const fullPath = location.hash.slice(1).split('?')[0];
    if (fullPath.startsWith('/board-service/list')) {
        return '/board-service/list';
    }
    return fullPath;
}

async function loadPage() {
    const key = parsePath();

    const {
        html: htmlPath = '/html/error/404.html',
        css:  cssPath  = null,
        js:   jsPath   = null
    } = routes[key] || {};

    // CSS 로드
    document.querySelectorAll('link[data-dynamic-css]').forEach(el => el.remove());
    if (cssPath) {
        const link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = `${cssPath}?t=${Date.now()}`;
        link.setAttribute('data-dynamic-css', '');
        document.head.appendChild(link);
    }

    // HTML 로드
    try {
        const res  = await fetch(htmlPath);
        const text = await res.text();
        const doc  = new DOMParser().parseFromString(text, 'text/html');
        document.getElementById('app').innerHTML = doc.body.innerHTML;
    } catch {
        document.getElementById('app').innerHTML = '<p>페이지를 로드할 수 없습니다.</p>';
    }

    // JS 로드 (import 구문이 있는 모듈 스크립트)
    document.querySelectorAll('script[data-dynamic-js]').forEach(el => el.remove());
    if (jsPath) {
        const script = document.createElement('script');
        script.type    = 'module';
        script.src     = `${jsPath}?t=${Date.now()}`;
        script.setAttribute('data-dynamic-js', '');
        document.body.appendChild(script);
    }

    // 공통 레이아웃 초기화
    await initLayoutSPA();
}

window.addEventListener('load', loadPage);
window.addEventListener('hashchange', loadPage);
