// Dummy data untuk history tontonan (misalnya)
const historyData = [
    { title: 'Movie 1', image: 'https://via.placeholder.com/200x300?text=Movie+1' },
    { title: 'Movie 2', image: 'https://via.placeholder.com/200x300?text=Movie+2' },
    { title: 'Movie 3', image: 'https://via.placeholder.com/200x300?text=Movie+3' },
    { title: 'Movie 4', image: 'https://via.placeholder.com/200x300?text=Movie+4' },
    { title: 'Movie 5', image: 'https://via.placeholder.com/200x300?text=Movie+5' }
  ];
  
  // Fungsi untuk menampilkan history tontonan
  function displayHistory() {
    const historySection = document.getElementById('history-section');
    historyData.forEach(movie => {
      const movieElement = document.createElement('div');
      movieElement.classList.add('movie');
      movieElement.innerHTML = `
        <img src="${movie.image}" alt="${movie.title}" style="width: 100%;">
        <p>${movie.title}</p>
      `;
      historySection.appendChild(movieElement);
    });
  }
  
  // Panggil fungsi displayHistory setelah halaman dimuat
  window.onload = displayHistory;
  