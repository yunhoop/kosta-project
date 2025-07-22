import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.8/+esm';
import { initHeader } from './header.js';

let cached = null;

async function loadLayoutFragment() {
    if (!cached) {
        const res  = await axios.get('/html/fragments/layout.html');
        const doc  = new DOMParser().parseFromString(res.data, 'text/html');
        cached = {
            headerHTML:  doc.querySelector('#header')?.innerHTML,
            sidebarHTML: doc.querySelector('.sideBar')?.innerHTML,
        };
    }

    const { headerHTML, sidebarHTML } = cached;
    const headerEl = document.querySelector('#header');
    const sideEl   = document.querySelector('.sideBar');
    if (headerEl && sideEl) {
        headerEl.innerHTML = headerHTML;
        sideEl.innerHTML   = sidebarHTML;
        requestAnimationFrame(() => initHeader());
    }
}

export async function initLayoutSPA() {
    await loadLayoutFragment();
}
export function initLayoutStatic() {
    loadLayoutFragment().catch(console.error);
}
