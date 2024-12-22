// Menangani klik pada tombol search
document.getElementById("search-toggle").addEventListener("click", function(event) {
    event.preventDefault();  // Mencegah aksi default
    const searchForm = document.getElementById("search-form");
    
    // Toggle tampilkan form pencarian
    searchForm.style.display = (searchForm.style.display === "block") ? "none" : "block";
  });

  const movieSection = document.querySelector('.movie-section');

  let isDown = false; 
  let startX; 
  let scrollLeft; 
  
  //saat mouse ditekan
  movieSection.addEventListener('mousedown', (e) => {
    isDown = true;
    movieSection.classList.add('active'); 
    startX = e.pageX - movieSection.offsetLeft; 
    scrollLeft = movieSection.scrollLeft; 
  });
  
  //saat mouse dilepas
  movieSection.addEventListener('mouseup', () => {
    isDown = false;
    movieSection.classList.remove('active'); 
  });
  
  //saat mouse bergerak
  movieSection.addEventListener('mousemove', (e) => {
    if (!isDown) return; 
    e.preventDefault();
    const x = e.pageX - movieSection.offsetLeft; 
    const walk = (x - startX) * 1; //drag speed
    movieSection.scrollLeft = scrollLeft - walk;
  });

  const progressContent = document.querySelector(".autoplay-progress span");

  const swiper = new Swiper(".mySwiper", {
      spaceBetween: 30,
      centeredSlides: true,
      autoplay: {
          delay: 10000000,
          disableOnInteraction: false,
      },
      pagination: {
          el: ".swiper-pagination",
          clickable: true,
      },
      navigation: {
          nextEl: ".swiper-button-next",
          prevEl: ".swiper-button-prev",
      }
      // on: {
      //     autoplayTimeLeft(s, time, progress) {
      //         progressContent.textContent = `${Math.ceil(time / 1000)}s`;
      //     },
      // },
  });
