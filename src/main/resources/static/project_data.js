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
    const url = `/board/${boardType}/${id}`;

    // {
    //   title:"반갑습니다. 제목입니다.",
    //   registeredAt: 1700373600000 || "2024-11-18T14:00:00Z",
    //   content: "코딩하기 되게 싫죠? 근데 뭐 안할수도 없고"
    //   views: 999 //조회수
    // }
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('게시글을 불러오는 데 문제가 발생했습니다.');
            }
            return response.json();
        })
        .then(importPosting)
        .catch(error => {
            console.error('문제가 발생했습니다:', error);
            alert('게시글을 불러오는 데 문제가 발생했습니다.');
        });
}
function importPosting(data) {
  if (data.success) {
    const post = data.data;
    document.getElementById('post-title').innerText = post.title;
    document.getElementById('post-date').innerText = new Date(post.registeredAt).toLocaleString();
    document.getElementById('post-content').innerText = post.content;
    document.getElementById('post-views').innerText = post.views;

    // 첨부파일 링크 추가
    const postFileElement = document.getElementById('post-file');
    postFileElement.innerHTML = '';  // 기존 내용을 지우기
    if (post.postImageUrlList && post.postImageUrlList.length > 0) {
      post.postImageUrlList.forEach((fileUrl, index) => {
        const link = createFileDownloaderElement(fileUrl,index);
        link.setAttribute('download', ''); // download 속성 추가
        postFileElement.appendChild(link);
      });
    }
  } else {
    alert(data.message);
  }
}

function createFileDownloaderElement(href,index){
  // <div class="notice-post-file" id="post-file">
  //   <img class="notice-post-date-image" src="img/file-icon.png">
  //   <span id="post-file-name">file.txt</span>
  // </div>
  const containerA = document.createElement('a');
    containerA.className = "notice-post-file";
    containerA.id="post-file";
    containerA.href = href;

  const img = document.createElement('img');
    img.className = 'notice-post-date-image';
    img.src = 'img/file-icon.png';

  const span = document.createElement('span');
    span.id = 'post-file-name';
    span.textContent = `첨부파일 ${index + 1}`;

  containerA.appendChild(img);
  containerA.appendChild(span);

  return containerA;
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
