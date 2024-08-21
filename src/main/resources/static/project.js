document.addEventListener("DOMContentLoaded", function() {
  loadBoardData();
});

function loadBoardData() {
  const data = JSON.parse(localStorage.getItem('project_boardData')) || [];
  const container = document.getElementById('board-container');
  container.innerHTML = ''; 
  
  data.forEach((item, index) => {
    const card = document.createElement('div');
    card.className = 'card';
    const imageUrl = item.image ? item.image : '../img/blackbug.png';
    card.innerHTML = `
    <img src="${imageUrl}" class="card-image">
    <div class="card-content">
      <h2>${item.제목}</h2>
      <p>${item.작성일}</p>
      </div>
      `
      card.addEventListener('click',function(){
        window.location.href =  `project_data.html?index=${index}`;
      });
      container.appendChild(card);
    });
}