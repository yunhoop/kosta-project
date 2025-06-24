// /js/layout-loader.js
import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.8/+esm';
import { initHeader } from '/js/header.js';

// 내부: layout.html 조각을 가져와 header와 sidebar에 삽입하고, header.js를 로드하여 initHeader() 호출
async function loadLayoutFragment() {
    try {
        const res = await axios.get('http://54.180.249.146:8880/html/fragments/layout.html');
        const doc = new DOMParser().parseFromString(res.data, 'text/html');

        const headerEl = document.querySelector('#header');
        const sideEl   = document.querySelector('.sideBar');
        if (headerEl && sideEl) {
            headerEl.innerHTML = doc.querySelector('#header').innerHTML;
            sideEl.innerHTML   = doc.querySelector('.sideBar').innerHTML;
        } else {
            console.warn('layout-container not found yet');
        }

        return new Promise((resolve, reject) => {
            const s = document.createElement('script');
            s.type    = 'module';
            s.src     = '/js/header.js';
            s.onload  = () => {
                initHeader();
                resolve();
            };
            s.onerror = reject;
            document.body.appendChild(s);
        });
    } catch (err) {
        console.error('layout fragment 로드 실패:', err);
    }
}

 // * SPA 전용: router.js에서 뷰 전환 시마다 호출
export async function initLayoutSPA() {
    await loadLayoutFragment();
}

 // * 일반 HTML 전용: DOMContentLoaded 이벤트에서 한 번만 호출
export function initLayoutStatic() {
    // 비동기 로드, 에러는 콘솔에만 표시
    loadLayoutFragment().catch(err => console.error('layout 로드 실패:', err));
}
