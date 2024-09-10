document.getElementById('modifyButton').addEventListener('click', async function() {
  const url = 'http://localhost:8080/REST_Aule_web/rest/eventi/modifica';

  // Recupera i dati dai campi del form
  const evento = {
    id: document.getElementById('ID').value,
    nome: document.getElementById('nome').value,
    oraInizio: document.getElementById('oraInizio').value,
    oraFine: document.getElementById('oraFine').value,
    descrizione: document.getElementById('descrizione').value,
    data: document.getElementById('data').value,
    tipologia: document.getElementById('tipologia').value,
    responsabileID: document.getElementById('responsabileID').value,
    corsoID: document.getElementById('corsoID').value,
    aulaID: document.getElementById('aulaID').value,
  };

  try {
    const response = await fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(evento)
    });

    if (response.ok) {
      document.getElementById('message').textContent = 'Evento modificato con successo!';
      document.getElementById('errorMessage').textContent = '';
    } else {
      throw new Error('Errore nella modifica dell\'evento.');
    }
  } catch (error) {
    document.getElementById('errorMessage').textContent = 'Errore: ' + error.message;
    document.getElementById('message').textContent = '';
  }
});

