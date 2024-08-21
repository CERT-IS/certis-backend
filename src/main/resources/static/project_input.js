document.addEventListener("DOMContentLoaded", function() {
  loadBoardData();
});

function addPost() {
  const title = document.getElementById('title').value;
  const content = document.getElementById('content').value;
  const author = '익명';
  const date = new Date().toISOString().split('T')[0];

  if (title && content) {
    const data = JSON.parse(localStorage.getItem('project_boardData')) || [];
    data.push({ 제목: title, 내용: content, 글쓴이: author, 작성일: date });
    localStorage.setItem('project_boardData', JSON.stringify(data));
    window.location.href = 'project.html';
  } else {
    alert('제목과 내용을 입력해주세요.');
  }
}