function validatePostForm() {
  const category = $('select[name="categoryId"]').val();
  if (!category) {
    alert("카테고리를 선택해주세요!");
    return false;
  }
  return true;
}
