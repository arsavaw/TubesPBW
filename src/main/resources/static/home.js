document.addEventListener('DOMContentLoaded', function() {
    initializeMovieScroll();
    initializeSearch();
    initializeDropdown();
    initializeSwiper();
});

function initializeMovieScroll() {
    const movieSection = document.querySelector('.movie-section');
    if (!movieSection) return;

    let isDown = false;
    let startX;
    let scrollLeft;

    movieSection.addEventListener('mousedown', (e) => {
        isDown = true;
        movieSection.classList.add('active');
        startX = e.pageX - movieSection.offsetLeft;
        scrollLeft = movieSection.scrollLeft;
    });

    movieSection.addEventListener('mouseleave', () => {
        isDown = false;
        movieSection.classList.remove('active');
    });

    movieSection.addEventListener('mouseup', () => {
        isDown = false;
        movieSection.classList.remove('active');
    });

    movieSection.addEventListener('mousemove', (e) => {
        if (!isDown) return;
        e.preventDefault();
        const x = e.pageX - movieSection.offsetLeft;
        const walk = (x - startX) * 1;
        movieSection.scrollLeft = scrollLeft - walk;
    });
}

function initializeSearch() {
    const searchToggle = document.getElementById('search-toggle');
    const searchModal = document.getElementById('searchModal');
    const closeBtn = searchModal ? searchModal.querySelector('.close') : null;

    searchToggle.addEventListener('click', (event) => {
        event.preventDefault();
        event.stopPropagation();
        searchModal.style.display = 'block';
    });

    closeBtn.addEventListener('click', () => {
        searchModal.style.display = 'none';
    });

    window.addEventListener('click', (event) => {
        if (event.target === searchModal) {
            searchModal.style.display = 'none';
        }
    });

    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape' && searchModal.style.display === 'block') {
            searchModal.style.display = 'none';
        }
    });
}

function initializeDropdown() {
    const dropdownToggle = document.querySelector('.dropdown-toggle');
    const dropdownMenu = document.querySelector('.dropdown-menu');

    if (!dropdownToggle || !dropdownMenu) {
        console.error('Dropdown elements not initialized');
        return;
    }

    dropdownToggle.addEventListener('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        dropdownMenu.classList.toggle('active');
    });

    document.addEventListener('click', function(e) {
        if (!dropdownMenu.contains(e.target) && !dropdownToggle.contains(e.target)) {
            dropdownMenu.classList.remove('active'); 
        }
    });

    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && dropdownMenu.classList.contains('active')) { 
            dropdownMenu.classList.remove('active'); 
        }
    });
}

function initializeSwiper() {
    const swiperElement = document.querySelector('.mySwiper');
    if (!swiperElement) return;

    new Swiper('.mySwiper', {
        spaceBetween: 30,
        centeredSlides: true,
        loop: true,
        autoplay: {
            delay: 5000,
            disableOnInteraction: false,
        },
        pagination: {
            el: '.swiper-pagination',
            clickable: true,
        },
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        }
    });
}