document.addEventListener('DOMContentLoaded', function() {
  let internalEvents = JSON.parse(localStorage.getItem('internalEvents')) || [];
  let currentDate = new Date();

  function generateUniqueId() {
    return '_' + Math.random().toString(36).substr(2, 9);
  }

  function createCalendar(calendarId, year, month, events) {
    const calendar = document.getElementById(calendarId);
    calendar.innerHTML = '';

    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const startDay = new Date(year, month, 1).getDay();

    for (let i = 0; i < startDay; i++) {
      const emptyCell = document.createElement('div');
      emptyCell.classList.add('day');
      calendar.appendChild(emptyCell);
    }

    for (let day = 1; day <= daysInMonth; day++) {
      const cell = document.createElement('div');
      cell.classList.add('day');

      const week = new Date(year, month, day);
      const dayofweek = week.getDay();

      if (dayofweek === 0 || dayofweek === 6) {
        cell.classList.add('weekend');
      }

      const header = document.createElement('div');
      header.classList.add('day-header');
      header.textContent = day;
      cell.appendChild(header);

      const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
      calendar.appendChild(cell);

      const dayEvents = events.filter(event => new Date(event.startDate) <= new Date(dateStr) && new Date(event.endDate) >= new Date(dateStr));

      dayEvents.forEach((element) => {
        const startDay = new Date(element.startDate).getDate();
        const endDay = new Date(element.endDate).getDate();

        const eventDiv = document.createElement('div');
        eventDiv.classList.add('multi-day');        
        eventDiv.style.backgroundColor = element.color; // 고정된 색상 적용
        eventDiv.style.marginLeft = '-1px'; // 이벤트를 오른쪽으로 약간 이동


        if (startDay === day && endDay === day) {
          eventDiv.style.borderRadius = '5px';
          eventDiv.style.width = 'calc(100% + 2px)';
          eventDiv.textContent = element.title;

          const deleteBtn = document.createElement('button');
          deleteBtn.classList.add('delete-btn'); 
          deleteBtn.textContent = 'x';
          deleteBtn.style.float = 'right';
          deleteBtn.onclick = function () {
            deleteEvent(element.id);
          };
          eventDiv.appendChild(deleteBtn);
        }
        else if (startDay === day) {
          eventDiv.style.borderTopLeftRadius = '5px';
          eventDiv.style.borderBottomLeftRadius = '5px';
          eventDiv.style.width = 'calc(100% + 2px)';
          eventDiv.textContent = element.title;

        } else if (endDay === day) {
          eventDiv.style.borderTopRightRadius = '5px';
          eventDiv.style.borderBottomRightRadius = '5px';
          eventDiv.style.width = 'calc(100% + 2px)';

          const deleteBtn = document.createElement('button');
          deleteBtn.classList.add('delete-btn'); 

          deleteBtn.textContent = 'x';
          deleteBtn.style.float = 'right';
          deleteBtn.onclick = function() {
              deleteEvent(element.id);
          };
          eventDiv.appendChild(deleteBtn);
        } else if (day > startDay && day < endDay) {
          eventDiv.style.width = 'calc(100% + 2px)';
        }

        cell.appendChild(eventDiv);    
      });
    }
  }

  function updateCalendarHeaders() {
    const options = { year: 'numeric', month: 'long' };
    document.getElementById('currentMonth').textContent = currentDate.toLocaleDateString('ko-KR', options);
  }

  function addEvent() {
    const title = document.getElementById('eventTitle').value;
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
  
    if (startDate && endDate && title) {
      const event = { 
        id: generateUniqueId(), 
        title, 
        startDate, 
        endDate, 
        color: 'rgb(255, 157, 157)' // 고정된 색상 
      };
  
      internalEvents.push(event);
      localStorage.setItem('internalEvents', JSON.stringify(internalEvents));
      createCalendar('calendar', currentDate.getFullYear(), currentDate.getMonth(), internalEvents);
      closeModal();
    } else {
      alert('제목과 날짜를 정확히 입력해주세요^^;;;');
    }
  }

  function deleteEvent(eventId){
    internalEvents = internalEvents.filter(event => event.id !== eventId);
    localStorage.setItem('internalEvents',JSON.stringify(internalEvents));
    createCalendar('calendar', currentDate.getFullYear(), currentDate.getMonth(), internalEvents);
  }

  function showModal(){
    document.getElementById('eventModal').style.display = 'flex';
  }

  function closeModal(){
    document.getElementById('eventModal').style.display = 'none';
  }
  
  document.getElementById('prevMonth').addEventListener('click', function() {
    currentDate.setMonth(currentDate.getMonth() - 1);
    createCalendar('calendar', currentDate.getFullYear(), currentDate.getMonth(), internalEvents);
    updateCalendarHeaders();
  });

  document.getElementById('nextMonth').addEventListener('click', function() {
    currentDate.setMonth(currentDate.getMonth() + 1);
    createCalendar('calendar', currentDate.getFullYear(), currentDate.getMonth(), internalEvents);
    updateCalendarHeaders();
  });

  createCalendar('calendar', currentDate.getFullYear(), currentDate.getMonth(), internalEvents);
  updateCalendarHeaders();
  window.addEvent = addEvent;


  document.getElementById('addEventButton').addEventListener('click', showModal);
  document.getElementById('saveEvent').addEventListener('click', addEvent);
  document.getElementById('closeModal').addEventListener('click', closeModal);

  createCalendar('calendar', currentDate.getFullYear(), currentDate.getMonth(), internalEvents);
  updateCalendarHeaders();
});
