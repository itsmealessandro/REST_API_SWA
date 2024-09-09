async function createEvento() {
  // Ottieni i valori dai campi del modulo
  const evento = {
    nome: document.getElementById('nome').value,
    oraInizio: document.getElementById('oraInizio').value,
    oraFine: document.getElementById('oraFine').value,
    descrizione: document.getElementById('descrizione').value,
    data: document.getElementById('data').value,
    tipologia: document.getElementById('tipologia').value,
    responsabileID: document.getElementById('responsabileID').value,
    corsoID: document.getElementById('corsoID').value,
    aulaID: document.getElementById('aulaID').value
  };

  const url = 'http://localhost:8080/REST_Aule_web/rest/eventi/nuovo';

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify(evento)
    });

    if (response.ok) {
      const result = await response.json();
      document.getElementById('response').textContent = 'Evento creato con successo: ' + JSON.stringify(result, null, 2);
    } else {
      document.getElementById('response').textContent = 'Errore nella creazione dell\'evento';
    }
  } catch (error) {
    document.getElementById('response').textContent = 'Errore: ' + error.message;
  }
}
