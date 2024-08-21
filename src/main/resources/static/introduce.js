function handleScroll() {
  const windowHeight = window.innerHeight;
 
  const header = document.querySelector('header');
  const headerHeight = header.offsetHeight;
  const triggerPoint = windowHeight - headerHeight;
  const scrollY = window.scrollY;
  if (scrollY >= triggerPoint) {
    header.style.top = '0px';
  } else {
    const newTop = Math.max(triggerPoint - scrollY, 0);
    header.style.top = `${newTop}px`;
    header.style.bottom = 'auto';
  }

  const section3 = document.querySelector('.section3');
  const section3Top = section3.getBoundingClientRect().top;
  if (section3Top < windowHeight) {
    section3.classList.add('visible');
  } 

  const appearanimations = document.querySelectorAll('.appearanimation');
  appearanimations.forEach((element) => {
    const elementTop = element.getBoundingClientRect().top;
    if (elementTop < windowHeight*0.75) {
      element.classList.add('visible');
    }
  });

  const upanimations = document.querySelectorAll('.upanimation');
  upanimations.forEach((element)=>{
  const elementTop = element.getBoundingClientRect().top;
  if(elementTop<windowHeight*0.75){
    element.classList.add('visible');
  }
 });
}

window.addEventListener('resize', handleResize);

function handleResize() {
  handleScroll();
}

window.addEventListener('load', function() {
  window.addEventListener('scroll', initialScroll, { once: true });
  handleScroll(); 
});



function initialScroll() {
  const header = document.querySelector('header');
  const windowHeight = window.innerHeight;
  const headerHeight = header.offsetHeight;

  const triggerPoint = windowHeight - headerHeight;

  window.scrollTo({
    top: triggerPoint,
    behavior: 'smooth'
  });

  window.removeEventListener('scroll', initialScroll);
  window.addEventListener('scroll', handleScroll);
}

