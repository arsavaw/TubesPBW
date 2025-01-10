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

    document.addEventListener('DOMContentLoaded', function() {
      var dropdownToggle = document.querySelector('.dropdown-toggle');
      var dropdownMenu = document.querySelector('.dropdown-menu');

      dropdownToggle.addEventListener('click', function(event) {
          event.preventDefault();
          dropdownMenu.classList.toggle('show');
      });

      document.addEventListener('click', function(event) {
          if (!dropdownToggle.contains(event.target)) {
              dropdownMenu.classList.remove('show');
          }
      });
    });
    

  const swiper = new Swiper(".mySwiper", {
    spaceBetween: 30,
    centeredSlides: true,
    loop: true,
    autoplay: {
        delay: 5000,
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
});

// Get DOM elements
const searchToggle = document.getElementById('search-toggle');
const searchModal = document.getElementById('searchModal');
const closeBtn = document.querySelector('.close');
const searchForm = document.getElementById('searchForm');
const searchResults = document.getElementById('searchResults');

// Show modal when search button is clicked
searchToggle.addEventListener('click', () => {
    searchModal.style.display = 'block';
});

// Close modal when X is clicked
closeBtn.addEventListener('click', () => {
    searchModal.style.display = 'none';
});

// Close modal when clicking outside
window.addEventListener('click', (event) => {
    if (event.target === searchModal) {
        searchModal.style.display = 'none';
    }
});

// Handle form submission
searchForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const formData = new FormData(searchForm);
    const params = new URLSearchParams(formData);
    
    try {
        const response = await fetch(`/search?${params.toString()}`);
        if (!response.ok) throw new Error('Search failed');
        
        const results = await response.json();
        displayResults(results);
    } catch (error) {
        console.error('Search error:', error);
        searchResults.innerHTML = '<p class="error">An error occurred while searching. Please try again.</p>';
    }
});

const dropdownToggle = document.querySelector('.dropdown-toggle');
const dropdownMenu = document.querySelector('.dropdown-menu');

dropdownToggle.addEventListener('click', function(e) {
    e.stopPropagation();
    dropdownMenu.classList.toggle('active');
});

// Close dropdown when clicking outside
document.addEventListener('click', function(e) {
    if (!dropdownMenu.contains(e.target) && !dropdownToggle.contains(e.target)) {
        dropdownMenu.classList.remove('active');
    }
});

// Close dropdown when pressing ESC key
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape' && dropdownMenu.classList.contains('active')) {
        dropdownMenu.classList.remove('active');
    }
});

function displayResults(movies) {
    if (!movies || movies.length === 0) {
        searchResults.innerHTML = '<p>No movies found matching your search criteria.</p>';
        return;
    }
    
    const resultsHtml = movies.map(movie => `
        <div class="movie">
            <img src="${movie.foto_Cover}" alt="${movie.nama_Film}">
            <div class="movie-info">
                <h3 class="movie-title">${movie.nama_Film}</h3>
                <div class="movie-meta">
                    <span class="genre">${movie.nama_Genre}</span>
                    <span class="stock">Stock: ${movie.stok}</span>
                </div>
                <p class="movie-description">${movie.deskripsiFilm}</p>
                ${movie.stok > 0 
                    ? `<a href="/movie/${movie.id_Film}" class="rent-btn">View Details</a>`
                    : '<p class="out-of-stock">Currently Unavailable</p>'
                }
            </div>
        </div>
    `).join('');
    
    searchResults.innerHTML = resultsHtml;
}