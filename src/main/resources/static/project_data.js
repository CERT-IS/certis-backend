document.addEventListener("DOMContentLoaded", function() {
  const urlParams = new URLSearchParams(window.location.search);
  const id = urlParams.get('id');
  loadPostDetail(id);

  document.getElementById('report-button').addEventListener('click', function() {
    Goreport();
  });
  document.getElementById('delete-button').addEventListener('click', function() {
    deletePost(id);
  });
});

function loadPostDetail(id) {
  const boardType = 'project';
  const url = `/${boardType}/${id}`;

  fetch(url)
      .then(response => {
        if (!response.ok) {
          throw new Error('게시글을 불러오는 데 문제가 발생했습니다.');
        }
        return response.json();
      })
      .then(data => {
        if (data.success) {
          const post = data.data;
          document.getElementById('post-title').innerText = post.제목;
          document.getElementById('post-date').innerText = post.작성일;
          document.getElementById('post-content').innerText = post.내용;
        } else {
          alert(data.message);
        }
      })
      .catch(error => {
        console.error('문제가 발생했습니다:', error);
        alert('게시글을 불러오는 데 문제가 발생했습니다.');
      });
}

function deletePost(id) {
  const boardType = 'noti';
  const accesstoken = localStorage.getItem('access-token');

  if (confirm('정말로 이 글을 삭제하시겠습니까?')) {
    fetch(`/${boardType}/delete/${id}`, {
      method: 'DELETE',
      headers: {
        'authorization-token': accesstoken
      }
    })
        .then(response => {
          if (!response.ok) {
            throw new Error('삭제 요청이 실패했습니다.');
          }
          return response.json();
        })
        .then(data => {
          alert(data.message);
          if (data.success) {
            window.location.href = 'project.html';
          }
        })
        .catch(error => {
          console.error('문제가 발생했습니다:', error);
          alert('게시글 삭제에 문제가 발생했습니다.');
        });
  }
}

function Goreport() {
  window.location.href = 'project.html';
}
