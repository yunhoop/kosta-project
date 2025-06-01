console.log("searchTag.js 로드됨");

// 전역으로 선언 (postPage.js와 공유)
var selectedTags = [];

$(document).ready(function () {
  const $input     = $(".inputTag input");
  const $tagList   = $(".tagList");
  const $applyBtn  = $(".applyTagBtn");
  const params     = new URLSearchParams(window.location.search);
  const categoryId = params.get("categoryId");
  console.log("tag categoryId:", categoryId);

  // 카테고리 5번(예: Q&A 등)은 태그 숨기기
  if (categoryId === "5") {
    $(".inputTag").hide();
    return;
  }

  $tagList.hide();

  // 1) 서버에서 태그 목록을 받아와서 렌더링
  function loadTags() {
    if (!categoryId) return console.error("searchTag: categoryId 없음");

    $.ajax({
      url: "controller",
      method: "GET",
      data: { cmd: "getTagList", categoryId },
      dataType: "json",
      success(resp) {
        if (typeof resp === "string") resp = JSON.parse(resp);

        $tagList.empty();
        resp.tagList.forEach(tag => {
          const $pill = $("<p>")
            .addClass("tag-pill")
            .text(tag.tagName);

          // 이전에 선택한 태그가 있으면 selected 클래스 추가
          if (selectedTags.indexOf(tag.tagName) > -1) {
            $pill.addClass("selected");
          }

          $pill.on("click", function () {
            const t = tag.tagName;
            const idx = selectedTags.indexOf(t);
            if (idx > -1) {
              selectedTags.splice(idx, 1);
              $pill.removeClass("selected");
            } else {
              if (selectedTags.length >= 6) {
                alert("태그는 최대 6개까지만 선택할 수 있습니다.");
                return;
              }
              selectedTags.push(t);
              $pill.addClass("selected");
            }
            // 선택된 태그를 input에 표시
            $input.val(selectedTags.map(x => `#${x}`).join(" "));
          });

          $tagList.append($pill);
        });

        console.log("✅ 태그 로드 완료:", resp.tagList);
      },
      error(xhr, status, error) {
        console.error("❌ 태그 로드 실패", error);
      }
    });
  }

  // 2) input 클릭 시 태그 박스 토글 & loadTags 호출
  $input.on("click", function (e) {
    e.stopPropagation();
    if ($tagList.is(":visible")) {
      $tagList.hide();
    } else {
      $tagList.css("display", "flex");
      loadTags();
    }
  });

  // 3) 바깥 클릭 시 태그 박스 숨기기
  $(document).on("click", function (e) {
    if (!$(e.target).closest("#section1").length) {
      $tagList.hide();
    }
  });

  // 4) 적용 버튼 클릭 시 전역 selectedTags 사용해 1페이지 재렌더링
  $applyBtn.on("click", function () {
    if (selectedTags.length === 0) {
      alert("먼저 태그를 선택해주세요!");
      return;
    }
    console.log("🔵 선택한 태그들:", selectedTags);
    // 전역 함수를 호출
    loadPage(1);
  });
});
