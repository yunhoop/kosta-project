function triggerImageUpload() {
  $("#imageInput").click();
}

$(document).ready(function () {
  $("#imageInput").on("change", function () {
    const file = this.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = function (event) {
      $("#previewImg").attr("src", event.target.result);
      $("#uploadGuide").hide();
      $("#imageFileNameDisplay").val(file.name);
    };
    reader.readAsDataURL(file);
  });
});
