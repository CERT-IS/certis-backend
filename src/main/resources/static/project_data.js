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

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function loadPostDetail(id) {
    const boardType = 'project';
    const url = `board/${boardType}/${id}`;

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
                document.getElementById('post-title').innerText = post.title;
                document.getElementById('post-date').innerText = new Date(post.registeredAt).toLocaleString();
                document.getElementById('post-content').innerText = post.content;

                // 첨부파일 링크 추가
                const postFileElement = document.getElementById('post-file');
                postFileElement.innerHTML = '';  // 기존 내용을 지우기
                if (post.postImageUrlList && post.postImageUrlList.length > 0) {
                    post.postImageUrlList.forEach((fileUrl, index) => {
                        const link = document.createElement('a');
                        link.href = fileUrl;
                        link.innerText = `첨부파일 ${index + 1}`;
                        link.setAttribute('download', ''); // download 속성 추가
                        postFileElement.appendChild(link);

                        // 줄바꿈을 위해 <br> 추가
                        postFileElement.appendChild(document.createElement('br'));
                    });
                } else {
                    postFileElement.innerText = '첨부파일 없음';
                }
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
  const boardType = 'project';
  const accesstoken = getCookie('refresh-token');

  if (confirm('정말로 이 글을 삭제하시겠습니까?')) {
    fetch(`/board/${boardType}/delete/${id}`, {
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
