document.addEventListener("DOMContentLoaded", function() {
    loadBoardData();
});

function loadBoardData() {
    const boardType = 'noti';
    const url = `/${boardType}/all`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('네트워크 응답이 좋지 않습니다.');
            }
            return response.json();
        })
        .then(data => {
            const tableBody = document.getElementById('board-body');
            if (!tableBody) {
                console.error('게시판 본문 요소를 찾을 수 없습니다.');
                return;
            }
            tableBody.innerHTML = '';

            const posts = data.data || [];
            if (!Array.isArray(posts)) {
                console.error('게시글 데이터가 배열이 아닙니다.', posts);
                return;
            }

            posts.forEach((item, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                <td>
                    <span style="font-weight:bold;font-size:1.5em; color:rgb(102, 102, 102);">${item.제목}</span><br><br>
                    <span style="color: rgb(155, 154, 154);">${item.작성일}</span>
                </td>
                `;
                row.addEventListener('click', function() {
                    window.location.href = `notice_data.html?id=${item.id}`;
                });
                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('문제가 발생했습니다:', error);
            alert('게시글을 불러오는 데 문제가 발생했습니다.');
        });
}
