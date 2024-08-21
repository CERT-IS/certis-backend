document.addEventListener("DOMContentLoaded", function() {
  const urlParams = new URLSearchParams(window.location.search);
  const index = urlParams.get('index');
  loadPostDetail(index);
  document.getElementById('report-button').addEventListener('click',function(){
    Goreport();
  });
  document.getElementById('delete-button').addEventListener('click', function() {
    deletePost(index);
  });
});

function loadPostDetail(index) {
  const data = JSON.parse(localStorage.getItem('boardData')) || [];
  if (data[index]) {
    document.getElementById('post-title').innerText = data[index].제목;
    document.getElementById('post-date').innerText = data[index].작성일;
    document.getElementById('post-content').innerText = data[index].내용;
  }
}

function deletePost(index) {
  if (confirm('정말로 이 글을 삭제하시겠습니까?')) {
    let data = JSON.parse(localStorage.getItem('notice_boardData')) || [];
    data.splice(index, 1);
    localStorage.setItem('notice_boardData', JSON.stringify(data));
    alert('글이 삭제되었습니다.');
    window.location.href = 'notice.html';
  }
}

function Goreport(){
  window.location.href = 'notice.html';
}