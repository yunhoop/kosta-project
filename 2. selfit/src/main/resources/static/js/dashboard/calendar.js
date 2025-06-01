document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('calendar');
    const panel = document.getElementById('foodDetailPanel');
    const selectedDateElem = document.getElementById('selectedDate');
    const foodListElem = document.getElementById('foodList');
    const totalCaloriesElem = document.getElementById('totalCalories');

    // 날짜별 음식 저장 객체
    const foodData = {
        '2025-05-20': [
            {name: '등심 돈까스', amount: '200g', kcal: 500},
            {name: '우동 정식', amount: '200g', kcal: 500}
        ],
        '2025-05-22': [
            {name: '삼겹살', amount: '400g', kcal: 5000}
        ]
    };

    const events = Object.entries(foodData).flatMap(([date, items]) => {
        return items.map(item => ({
            title: `${item.name} • ${item.kcal}kcal`,
            start: date,
            allDay: true
        }));
    });

    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'ko', // 한국어 설정
        events: events,
        headerToolbar: {
            left: 'prev,next',    // ✅ today 제거
            center: 'title',
            right: ''             // 필요 시 다른 버튼 추가 가능
        },
        eventDisplay: 'block',
        dateClick: function (info) {
            const clickedDate = info.dateStr;
            selectedDateElem.textContent = clickedDate.replace(/-/g, '.');
            panel.style.display = 'block';
            panel.setAttribute('data-date', clickedDate);

            calendarEl.classList.add('shrinked');

            renderFoodList(clickedDate);
        },


    });

    calendar.render();


    function renderFoodList(dateStr) {
        const foods = foodData[dateStr] || [];
        foodListElem.innerHTML = '';
        let total = 0;

        foods.forEach((food, i) => {
            const li = document.createElement('li');
            li.innerHTML = `
            <div class="food-row">
                <div class="food-name">${food.name}</div>
                <div class="food-amount">${food.amount}</div>
                <div class="food-kcal">${food.kcal}kcal</div>
                <button class="update-btn">
                    <i class="bi bi-pencil-square upd-del"></i>
                </button>
                <button class="delete-btn" onclick="removeFood('${dateStr}', ${i})">
                    <i class="bi bi-x-square upd-del"></i>
                </button>
            </div>
      `;
            foodListElem.appendChild(li);
            total += food.kcal;
        });

        totalCaloriesElem.textContent = `총 ${total}kcal`;
    }

    window.addFood = function () {
        const name = document.getElementById('foodName').value;
        const amount = document.getElementById('foodAmount').value;
        const kcal = 500; // 임시 값
        const dateStr = panel.getAttribute('data-date');

        if (!foodData[dateStr]) foodData[dateStr] = [];
        foodData[dateStr].push({name, amount, kcal});

        document.getElementById('foodName').value = '';
        document.getElementById('foodAmount').value = '';
        renderFoodList(dateStr);
    };

    window.removeFood = function (dateStr, index) {
        foodData[dateStr].splice(index, 1);
        renderFoodList(dateStr);

        calendar.render();
    };
    //  닫기 버튼 기능
    const closeBtn = document.getElementById('closePanelBtn');
    closeBtn.addEventListener('click', function () {
        panel.style.display = 'none';
        calendarEl.classList.remove('shrinked'); // 원래 크기로 복원
    });

});
