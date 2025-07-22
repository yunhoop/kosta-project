$(document).ready(function () {
  fetchNoticeList();
});

function fetchNoticeList() {
  $.ajax({
    url: "controller?cmd=noticeAction",
    method: "GET",
    dataType: "json",
    success: function (data) {
      renderNotices(data);
    },
    error: function () {
      console.error("공지사항 불러오기 실패");
    }
  });
}

function renderNotices(noticeList) {
  const $container = $(".accordion");
  $container.empty();

  noticeList.forEach(function (notice) {
    const $item = $("<div>").addClass("accordion-item");

    const $headerWrapper = $("<div>").addClass("accordion-header-wrapper");
    const $header = $("<button>")
      .addClass("accordion-header")
      .text("[" + notice.noticeId + "] " + notice.noticeName);

    $headerWrapper.append($header);

    const $body = $("<div>")
      .addClass("accordion-body")
      .text(notice.noticeContents)
      .hide();

    $header.on("click", function () {
      const isOpen = $item.hasClass("active");
      $(".accordion-item").removeClass("active").find(".accordion-body").slideUp();
      if (!isOpen) {
        $item.addClass("active");
        $body.slideDown();
      }
    });

    $item.append($headerWrapper).append($body);
    $container.append($item);
  });
}
