document.addEventListener("DOMContentLoaded", function() {
  fetch('/navi.html')
      .then(response => response.text())
      .then(data => {
          document.getElementById('header-placeholder').innerHTML = data;
          document.getElementById("certis-button").addEventListener("click", function() {
              window.location.href = "";
          });
      });

     
});
