// Menangani klik pada tombol search
document.getElementById("search-toggle").addEventListener("click", function(event) {
    event.preventDefault();  // Mencegah aksi default
    const searchForm = document.getElementById("search-form");
    
    // Toggle tampilkan form pencarian
    searchForm.style.display = (searchForm.style.display === "block") ? "none" : "block";
  });
  