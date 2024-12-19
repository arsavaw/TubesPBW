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