$(document).ready(function () {
  const $categorySelect = $("select[name='categoryId']");
  const $tagContainer = $(".tag-grid");
  const $selectedTags = $("#selectedTags");

  $categorySelect.on("change", function () {
    const categoryId = $(this).val();

    $tagContainer.empty();
    $selectedTags.empty();

    if (!categoryId) return;

    $.ajax({
      url: "controller",
      method: "GET",
      data: {
        cmd: "tagListAction",
        categoryId: categoryId
      },
      success: function (data) {
        const $rowTop = $("<div>").addClass("row row-top");
        const $rowBottom = $("<div>").addClass("row row-bottom");

        data.forEach((tag, index) => {
          const $btn = $("<button>")
            .attr("type", "button")
            .addClass("tag")
            .data("tagid", tag.tagId)
            .text(`#${tag.tagName}`);

          (index < 6 ? $rowTop : $rowBottom).append($btn);
        });

        $tagContainer.append($rowTop, $rowBottom);
      },
      error: function () {
        alert("태그 정보를 불러오는 데 실패했습니다.");
      }
    });
  });
});
