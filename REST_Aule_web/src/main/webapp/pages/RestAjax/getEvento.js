async function getEventoByID() {

  // Ottieni l'ID dall'input dell'utente
  const eID = document.getElementById('eventId').value;
  const url = `http://localhost:8080/REST_Aule_web/rest/eventi/${eID}`;

  // Debug: stampa l'URL nella console
  console.log('URL:', url);

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      }
    });

    if (response.ok) {
      const evento = await response.json();
      document.getElementById('eventDetails').textContent = JSON.stringify(evento, null, 2);
    } else if (response.status === 404) {
      document.getElementById('eventDetails').textContent = 'Evento non trovato';
    } else {
      document.getElementById('eventDetails').textContent = 'Errore nel recupero dei dati';
    }
  } catch (error) {
    document.getElementById('eventDetails').textContent = 'Errore: ' + error.message;
  }
}
