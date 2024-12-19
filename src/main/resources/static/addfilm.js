document.addEventListener('DOMContentLoaded', () => {
    const genreSelect = document.getElementById('genre');
    const form = document.getElementById('add-film-form');
    const messageDiv = document.getElementById('message');

    // Fetch genres and populate the dropdown
    fetch('/api/genres')
        .then(response => response.json())
        .then(genres => {
            genres.forEach(genre => {
                const option = document.createElement('option');
                option.value = genre.id;
                option.textContent = genre.nama;
                genreSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching genres:', error));

    // Handle form submission
    form.addEventListener('submit', (event) => {
        event.preventDefault();

        const namaFilm = document.getElementById('nama-film').value;
        const idGenre = genreSelect.value;
        const actors = document.getElementById('actors').value.split(',').map(actor => actor.trim());

        const filmData = {
            namaFilm,
            idGenre,
            actors
        };

        fetch('/api/films', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(filmData)
        })
        .then(response => {
            if (response.ok) {
                messageDiv.textContent = 'Film berhasil ditambahkan!';
                form.reset();
            } else {
                return response.json().then(error => {
                    messageDiv.textContent = `Error: ${error.message}`;
                });
            }
        })
        .catch(error => {
            console.error('Error adding film:', error);
            messageDiv.textContent = 'Terjadi kesalahan saat menambahkan film.';
        });
    });
});
