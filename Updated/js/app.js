document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const usernameField = document.getElementById('username');
    const passwordField = document.getElementById('password');

    loginForm.addEventListener('submit', (e) => {
        e.preventDefault();

        const username = usernameField.value.trim();
        const password = passwordField.value.trim();

        if (username === "" || password === "") {
            alert("Username dan password tidak boleh kosong.");
            return;
        }

        // Logika login - sementara ini hanya pengecekan sederhana
        if (username === "admin" && password === "admin123") {
            // Simulasikan login sukses
            alert("Login berhasil!");
            // Anda bisa mengarahkan pengguna ke halaman utama aplikasi setelah login
            window.location.href = "dashboard.html"; // Ganti sesuai halaman utama
        } else {
            alert("Username atau password salah.");
        }
    });
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
