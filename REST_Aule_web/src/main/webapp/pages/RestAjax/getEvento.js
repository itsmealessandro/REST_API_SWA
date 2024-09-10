document.getElementById('fetchButton').addEventListener('click', async function() {
  console.log("premuto");
  const eID = document.getElementById('eID').value;

  if (!eID) {
    document.getElementById('errorMessage').textContent = 'Per favore, inserisci l\'ID dell\'evento.';
    return;
  }

  const url = `http://localhost:8080/REST_Aule_web/rest/eventi/${eID}`;

  try {
    const response = await fetch(url);
    if (response.ok) {
      const evento = await response.json();
      const eventDetails = document.getElementById('eventDetails');
      eventDetails.innerHTML = ''; // Svuota i dettagli esistenti

      // Aggiungi i dettagli dell'evento alla pagina
      eventDetails.innerHTML = `
                        <p><strong>ID:</strong> ${evento.ID}</p>
                        <p><strong>Nome:</strong> ${evento.nome}</p>
                        <p><strong>Ora Inizio:</strong> ${evento.oraInizio}</p>
                        <p><strong>Ora Fine:</strong> ${evento.oraFine}</p>
                        <p><strong>Descrizione:</strong> ${evento.descrizione}</p>
                        <p><strong>Ricorrenza:</strong> ${evento.ricorrenza}</p>
                        <p><strong>Data:</strong> ${evento.data}</p>
                        <p><strong>Tipologia:</strong> ${evento.tipologia}</p>
                        <p><strong>Responsabile ID:</strong> ${evento.responsabileID}</p>
                        <p><strong>Corso ID:</strong> ${evento.corsoID}</p>
                        <p><strong>Aula ID:</strong> ${evento.aulaID}</p>
                    `;

    } else {
      throw new Error('Errore nel recupero dei dettagli dell\'evento.');
    }
  } catch (error) {
    document.getElementById('errorMessage').textContent = 'Errore: ' + error.message;
  }
});
