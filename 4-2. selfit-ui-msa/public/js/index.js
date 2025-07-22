document.addEventListener('DOMContentLoaded', function () {
    const featureCards = document.querySelectorAll('.feature-card');

    featureCards.forEach(card => {
        card.addEventListener('click', function () {
            const action = this.getAttribute('data-action');

            if (action === 'dashboard') {

                window.location.href = 'dashboard/dashboard.html';

            } else if (action === 'community') {
                // 1) API에서 카테고리 목록 받아오기
                axios.get('http://54.180.249.146:8881/api/category')
                    .then(res => {
                        const categories = res.data;
                        if (!categories.length) {
                            console.warn('카테고리 없음');
                            return;
                        }
                        // 2) 기본으로 첫 번째 카테고리 ID 사용 (필요시 다른 로직 적용)
                        const defaultCategoryId = categories[0].categoryId;
                        // 3) SPA 페이지로 해시 + 쿼리스트링 붙여 이동
                        window.location.href = `/html/spa.html#/board/list?categoryId=${defaultCategoryId}`;
                    })
                    .catch(err => {
                        console.error('카테고리 조회 실패:', err);
                    });

            }
        });
    });
});
