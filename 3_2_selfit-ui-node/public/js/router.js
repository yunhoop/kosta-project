import { initLayoutSPA  } from '/js/layout-loader.js';

const routes = {
    '/board/write':  { html: '/html/board/boardForm.html',    css: '/css/board/boardWrite.css'   },
    '/board/edit':   { html: '/html/board/boardForm.html',    css: '/css/board/boardWrite.css'   },
    '/board/list':   { html: '/html/board/board.html',        css: '/css/board/board.css'       },
    '/board/detail': { html: '/html/board/boardDetail.html',  css: '/css/board/boardDetail.css' },
};

function parsePath() {
    return (location.hash.slice(1) || '/board/list').split('?')[0];
}

async function loadPage() {
    const pathInfo = routes[parsePath()] || {};
    const htmlPath = pathInfo.html || '/html/error/404.html';
    const cssPath  = pathInfo.css  || null;

    // CSS 교체
    document.querySelectorAll('link[data-dynamic-css]').forEach(el => el.remove());
    if (cssPath) {
        const link = document.createElement('link');
        link.rel  = 'stylesheet';
        link.href = `${cssPath}?t=${Date.now()}`;
        link.setAttribute('data-dynamic-css', '');
        document.head.appendChild(link);
    }

    // HTML fragment 로드
    let fragment;
    try {
        const res  = await fetch(htmlPath);
        const text = await res.text();
        const doc  = new DOMParser().parseFromString(text, 'text/html');
        fragment   = doc.body.innerHTML;
    } catch {
        fragment = '<p>페이지를 로드할 수 없습니다.</p>';
    }

    // #app 에 삽입
    document.getElementById('app').innerHTML = fragment;

    // 공통 레이아웃 주입
    await initLayoutSPA ();

    // 뷰 스크립트 재실행
    let viewScript = null;
    if (htmlPath.includes('boardForm')) viewScript = '/js/board/boardForm.js';
    if (htmlPath.includes('board.html')) viewScript = '/js/board/board.js';
    if (htmlPath.includes('boardDetail.html')) viewScript = '/js/board/boardDetail.js';

    if (viewScript) {
        document.querySelectorAll(`script[src^="${viewScript}"]`).forEach(el => el.remove());
        const s = document.createElement('script');
        s.type = 'module';
        s.src  = `${viewScript}?t=${Date.now()}`;
        document.body.appendChild(s);
    }
}

window.addEventListener('load',      loadPage);
window.addEventListener('hashchange', loadPage);
