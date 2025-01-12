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

  window.onload = displayHistory;
  