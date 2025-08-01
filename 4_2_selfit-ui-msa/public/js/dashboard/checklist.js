function parseJwtMemberId(token) {
    if (!token) return null;
    try {
        const base64Url = token.split('.')[1];
        const base64    = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const json      = decodeURIComponent(
            atob(base64)
                .split('')
                .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );
        return JSON.parse(json).memberId;
    } catch (e) {
        console.error('Invalid JWT:', e);
        return null;
    }
}
const token = localStorage.getItem('auth');
if ( token === null ) {
    location.replace('/html/account/login.html');
}

const memberId  = parseJwtMemberId(token);
if (!memberId) {
    localStorage.removeItem('auth');
    location.replace('/html/account/login.html');
}

let calendar;
let currentSelectedDate = null;
let currentChecklistId = null;
const checklistData = {};
const checklistIdMap = {}; // { "YYYY-MM-DD": [ { checkId, checklistId, text, completed } ] }
let checklistList = [];
let editIndex = null;
let itemToDeleteIndex = null;


// Axios 기본 설정
document.addEventListener('DOMContentLoaded', async () => {
axios.defaults.baseURL = 'http://127.0.0.1:8000/api/checklist-service';
axios.defaults.headers.common['selfitKosta'] = `Bearer ${token}`;
axios.defaults.headers.common['Content-Type'] = 'application/json';
axios.defaults.withCredentials = true;

function pruneChecklistData(year, month) {
    for (const key in checklistData) {
        const date = new Date(key);
        if (date.getFullYear() !== year || date.getMonth() + 1 !== month) {
            delete checklistData[key];
        }
    }
}

async function loadChecklistBetween(startDate, endDate) {
    const promises = [];
    const cursor = new Date(startDate);

    while (cursor <= endDate) {
        const yyyy = cursor.getFullYear();
        const mm = String(cursor.getMonth() + 1).padStart(2, '0');
        const dd = String(cursor.getDate()).padStart(2, '0');
        const dateStr = `${yyyy}-${mm}-${dd}`;

        if (checklistData[dateStr]) {
            cursor.setDate(cursor.getDate() + 1);
            continue; // 이미 로드된 날짜는 skip
        }

        promises.push(
            axios.post(`/member/${memberId}`, { checklistDate: dateStr })
                .then(res => {
                    checklistData[dateStr] = res.data.map(item => ({
                        id: item.id,
                        checklistId: item.checklistId,
                        text: item.checklistContent,
                        completed: item.isChecked === 1
                    }));
                    if (res.data.length > 0) {
                        checklistIdMap[dateStr] = res.data[0].checklistId;
                    }
                })
                .catch(() => {
                    delete checklistData[dateStr];
                })
        );

        cursor.setDate(cursor.getDate() + 1);
    }

    await Promise.all(promises);
}

// 해당 연-월의 체크리스트 데이터를 서버에서 불러와 checklistData에 저장
async function loadMonthlyChecklist(year, month) {
    const daysInMonth = new Date(year, month, 0).getDate();
    const promises = [];
    for (let day = 1; day <= daysInMonth; day++) {
        const date = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        promises.push(
            axios.post(`/member/${memberId}`, {checklistDate: date})
                .then(res => {
                    checklistData[date] = res.data.map(item => ({
                        id: item.id,
                        checklistId: item.checklistId,
                        text: item.checklistContent,
                        completed: item.isChecked === 1
                    }));
                    if (res.data.length > 0) {
                        checklistIdMap[date] = res.data[0].checklistId; // ✅ 이 줄 추가
                    }
                })
                .catch(() => {
                    delete checklistData[date]; // <- 아예 제거하도록 수정
                })
        );
    }
    await Promise.all(promises);
}

// FullCalendar 이벤트 데이터 생성 (최대 3개 아이템)
function buildCalendarEvents() {
    return Object.entries(checklistData)
        .filter(([_, items]) => items && items.length)
        .map(([date, items]) => {
            const html = items.slice(0, 3).map(item =>
                `<div class="check-item-box d-flex align-items-center mb-1">
           <i class="bi ${item.completed ? 'bi-check-square text-success' : 'bi-square'}"></i>
           <span class="check-text text-truncate" title="${item.text}">${item.text}</span>
         </div>`
            ).join('');
            return {start: date, title: 'check', allDay: true, display: 'block', extendedProps: {checklistHTML: html}};
        });
}

// 패널 목록 렌더링 (수정 중인 행에 .editing 클래스)
function renderChecklistList() {
    const listEl = document.getElementById('check-list');
    if (!listEl || !currentSelectedDate) return;
    listEl.innerHTML = '';
    checklistList = checklistData[currentSelectedDate] || [];

    checklistList.forEach((item, idx) => {
        const row = document.createElement('div');
        row.className = 'check-row d-flex justify-content-between align-items-center';
        if (idx === editIndex) row.classList.add('editing');
        row.innerHTML = `
      <div class="d-flex align-items-center gap-2">
        <i class="bi ${item.completed ? 'bi-check2 text-success' : ''} toggle-check" data-index="${idx}" role="button"></i>
        <span class="flex-grow-1 ${item.completed ? 'text-decoration-line-through text-muted' : ''}">${item.text}</span>
      </div>
      <div class="d-flex gap-2">
        <i class="bi bi-pencil-square edit-btn" data-index="${idx}" role="button"></i>
        <i class="bi bi-x-square delete-btn" data-index="${idx}" data-bs-toggle="modal" data-bs-target="#delete-modal" role="button"></i>
      </div>
    `;
        listEl.appendChild(row);
    });
}

// 수정 버튼 클릭 핸들러
function onEditClick(e) {
    const idx = Number(e.target.dataset.index);
    editIndex = idx;
    document.getElementById('check-name').value = checklistList[idx].text;
    document.getElementById('add-check-btn').innerText = '수정';
    renderChecklistList();
}

// 체크 토글 핸들러
async function onCheckboxChange(e) {
    const idx = Number(e.target.dataset.index);
    const item = checklistList[idx];
    const completed = !item.completed;
    await axios.put(`/item/checklist/member/${memberId}`, {checklistId: item.checklistId, isChecked: completed ? 1 : 0});
    item.completed = completed;
    renderChecklistList();
    calendar.refetchEvents();
}

// 패널 닫기
document.getElementById('close-panel-btn').addEventListener('click', async () => {
    document.getElementById('check-panel').style.display = 'none';

    // 체크 항목이 없고 checklistId가 있을 경우만 삭제
    // if (checklistList.length === 0 && currentChecklistId) {
    //     try {
    //         await axios.delete(`/item/checklist/member/${memberId}`, {
    //             data: { checklistId: currentChecklistId }  // ✅ 객체로 감싸서 보냄
    //         });
    //
    //         // 프론트에서 데이터 제거
    //         delete checklistData[currentSelectedDate];
    //         delete checklistIdMap[currentSelectedDate];
    //
    //         // 달력 리렌더링
    //         calendar.refetchEvents();
    //     } catch (err) {
    //         console.error('체크리스트 삭제 실패:', err);
    //         alert('체크리스트 삭제 중 오류가 발생했습니다.');
    //     }
    // }

    // 상태 초기화
    currentSelectedDate = null;
    currentChecklistId = null;
});

// 등록/수정 핸들러
async function onAddOrEdit() {
    const input = document.getElementById('check-name');
    const text = input.value.trim();
    if (!text || !currentSelectedDate || !currentChecklistId) return;

    // 수정 모드일 때
    if (typeof editIndex === 'number' && checklistList[editIndex]) {
        const item = checklistList[editIndex];


        // 로컬 데이터 갱신
        item.text = text;
        checklistList[editIndex] = {
            id: item.id,
            checklistId: item.checklistId,
            text: item.text,
            completed: item.completed
        };
        checklistData[currentSelectedDate] = [...checklistList];

        // 상태 초기화
        editIndex = null;
        document.getElementById('add-check-btn').innerText = '등록';
    } else {
        // 추가 모드
        const res = await axios.post(`/item/member/${memberId}`, {
            checklistDate: currentSelectedDate,
            checklistContent: text,
            isChecked: 0
        });
        currentChecklistId = res.data;

        const newItem = {
            checklistId: res.data,
            text: text,
            completed: false
        };

        // 리스트에 추가
        checklistList.push(newItem);
        checklistData[currentSelectedDate] = [...checklistList];
    }

    // 입력창 초기화 및 UI 갱신
    input.value = '';
    renderChecklistList();
    calendar.refetchEvents();
}

// 삭제 확정 핸들러
async function onDeleteConfirmed() {
    if (itemToDeleteIndex === null) return;
    const item = checklistList[itemToDeleteIndex];
    console.log('삭제 요청 아이템', item);
    await axios.delete(`/item/checklist/member/${memberId}`, {data: {checklistId: item.checklistId}});
    checklistList.splice(itemToDeleteIndex, 1);
    checklistData[currentSelectedDate] = checklistList;
    renderChecklistList();
    calendar.refetchEvents();
    bootstrap.Modal.getInstance(document.getElementById('delete-modal')).hide();
    itemToDeleteIndex = null;
}

// 날짜 클릭 핸들러: 패널 열고 상세 로드
    async function onDateClick(info) {
        currentSelectedDate = info.dateStr;

        // ─ UI 초기화 ─────────────────────────────────────
        document.getElementById('panel-date').innerText = currentSelectedDate.replace(/-/g, '.');
        document.getElementById('check-panel').style.display = 'block';
        editIndex = null;
        document.getElementById('add-check-btn').innerText = '등록';
        document.getElementById('check-name').value = '';
        // ────────────────────────────────────────────────

        try {
            /* 1) 당일 체크리스트-아이템 조회 */
            const itemsRes = await axios.post(`/member/${memberId}`, {
                checklistDate: currentSelectedDate
            });

            checklistList = itemsRes.data.map(i => ({
                id          : i.id,
                checklistId : i.checklistId,
                text        : i.checklistContent,
                completed   : i.isChecked === 1
            }));
            checklistData[currentSelectedDate] = checklistList;

            /* 2) checklistId 결정 */
            if (checklistList.length > 0) {
                // (기존) 이미 아이템이 있으면 그 id 사용
                currentChecklistId = checklistList[0].checklistId;
            } else if (checklistIdMap[currentSelectedDate]) {
                // (기존) 과거에 생성된 머리글이 캐시에 있으면 사용
                currentChecklistId = checklistIdMap[currentSelectedDate];
            } else {
                // ⭐ 새 날짜라면 머리글(checklist) 먼저 생성
                const createRes      = await axios.post(`/member/${memberId}`, {
                    checklistDate: currentSelectedDate
                });
                currentChecklistId   = createRes.data;
                checklistIdMap[currentSelectedDate] = currentChecklistId;
            }

            renderChecklistList();
        } catch (err) {
            console.error('[onDateClick] 오류:', err);
        }
    }

// 초기화 및 FullCalendar 설정

    const calendarEl = document.getElementById('calendar');
    if (!calendarEl) return;
    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth', locale: 'ko', height: 650,
        headerToolbar: {left: 'prev,next today', center: 'title', right: ''}, buttonText: {today: '오늘'},
        eventDisplay: 'block',
        events: async (fetchInfo, success) => {
            const start = new Date(fetchInfo.start);
            const end = new Date(fetchInfo.end);

            await loadChecklistBetween(start, end);
            success(buildCalendarEvents());
        },
        eventContent: info => {
            const d = document.createElement('div');
            d.innerHTML = info.event.extendedProps.checklistHTML || '';
            return {domNodes: [d]};
        },
        dateClick: onDateClick
    });
    calendar.render();
    calendar.refetchEvents();

    // 패널 인터랙션 바인딩
    document.getElementById('add-check-btn').addEventListener('click', onAddOrEdit);
    document.getElementById('check-list').addEventListener('click', e => {
        if (e.target.classList.contains('edit-btn')) onEditClick(e);
        if (e.target.classList.contains('delete-btn')) itemToDeleteIndex = Number(e.target.dataset.index);
        if (e.target.classList.contains('toggle-check')) onCheckboxChange(e);
    });
    document.getElementById('confirm-delete-btn').addEventListener('click', onDeleteConfirmed);
    document.getElementById('close-panel-btn').addEventListener('click', () => {
        document.getElementById('check-panel').style.display = 'none';
    });
});
