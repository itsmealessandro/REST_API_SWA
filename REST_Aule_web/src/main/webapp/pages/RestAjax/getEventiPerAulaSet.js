document.getElementById('fetchButton').addEventListener('click', async function() {
  const aulaID = document.getElementById('aulaID').value;
  const data = document.getElementById('data').value;

  if (!aulaID || !data) {
    document.getElementById('errorMessage').textContent = 'Per favore inserisci sia l\'ID dell\'aula che la data.';
    return;
  }

  const url = `http://localhost:8080/REST_Aule_web/rest/collezioni/aule/${aulaID}/settimana/${data}`;

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

