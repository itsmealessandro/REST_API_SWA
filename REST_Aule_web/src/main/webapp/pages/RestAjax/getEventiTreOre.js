document.getElementById('fetchButton').addEventListener('click', async function() {
  const url = 'http://localhost:8080/REST_Aule_web/rest/collezioni/eventiTreOre';

  try {
    const response = await fetch(url);
    if (response.ok) {
      const data = await response.json();
      const urlList = document.getElementById('urlList');
      urlList.innerHTML = ''; // Svuota la lista esistente

      data.forEach(url => {
        const li = document.createElement('li');
        const a = document.createElement('a');
        a.href = url;
        a.textContent = url;
        li.appendChild(a);
        urlList.appendChild(li);
      });
    } else {
      throw new Error('Errore nel recupero degli eventi.');
    }
  } catch (error) {
    document.getElementById('errorMessage').textContent = 'Errore: ' + error.message;
  }
});

