// Funzione per gestire l'importazione del CSV
function uploadCSV() {
  const fileInput = document.getElementById('fileInput'); // Ottiene l'elemento input di tipo file
  const file = fileInput.files[0];

  if (!file) {
    alert('Per favore, seleziona un file CSV.');
    return;
  }

  const reader = new FileReader();

  reader.onload = function() {
    const csvData = reader.result;

    fetch('http://localhost:8080/REST_Aule_web/rest/collezioni/importazione', {
      method: 'POST',
      headers: {
        'Content-Type': 'text/csv' // Imposta il tipo di contenuto su text/csv
      },
      body: csvData
    })
      .then(response => {
        if (response.ok) {
          return response.text(); // Legge la risposta come testo
        } else {
          throw new Error('Errore nella richiesta');
        }
      })
      .then(data => {
        // Mostra un messaggio di successo o il contenuto della risposta
        document.getElementById('message').innerHTML = `<span class="success">${data}</span>`;
      })
      .catch(error => {
        // Mostra un messaggio di errore
        document.getElementById('message').innerHTML = `<span class="error">${error.message}</span>`;
      });
  };

  reader.readAsText(file); // Legge il file come testo
}
