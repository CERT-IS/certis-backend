document.addEventListener("DOMContentLoaded", function() {
  const addButton = document.getElementById('addPostButton');
  if (addButton) {
    addButton.onclick = addPost;
  }
});

function addPost() {
  const title = document.getElementById('title').value;
  const content = document.getElementById('content').value;
  const filesInput = document.getElementById('files');
  const files = filesInput ? filesInput.files : [];
  const accesstoken = localStorage.getItem('access-token');
  const boardType = 'project';

  if (title && content) {
    const postDto = {
      제목: title,
      내용: content
    };

    const formData = new FormData();
    formData.append('postDto', new Blob([JSON.stringify(postDto)], { type: 'application/json' }));

    if (files.length > 0) {
      Array.from(files).forEach(file => {
        formData.append('files', file);
      });
    }

    fetch(`/${boardType}/write`, {
      method: 'POST',
      headers: {
        'authorization-token': accesstoken
      },
      body: formData
    })
        .then(response => {
          if (!response.ok) {
            throw new Error('게시글 작성 요청이 실패했습니다.');
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
          alert('게시글 작성에 문제가 발생했습니다.');
        });
  } else {
    alert('제목과 내용을 입력해주세요.');
  }
}
