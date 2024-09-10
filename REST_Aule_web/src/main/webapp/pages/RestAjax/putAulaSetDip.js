document.getElementById('updateButton').addEventListener('click', async function() {
  const aulaID = document.getElementById('aulaID').value;
  const dipartimentoID = document.getElementById('dipartimentoID').value;

  if (!aulaID || !dipartimentoID) {
    document.getElementById('message').textContent = 'Per favore, inserisci sia l\'ID dell\'aula che l\'ID del dipartimento.';
    document.getElementById('message').className = 'message error';
    return;
  }

  const url = 'http://localhost:8080/REST_Aule_web/rest/aule/modifica';

  const data = {
    id: aulaID,
    dipartimentoID: dipartimentoID
  };

  try {
    const response = await fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    });

    if (response.ok) {
      document.getElementById('message').textContent = 'Dipartimento dell\'aula modificato con successo.';
      document.getElementById('message').className = 'message success';
    } else {
      throw new Error('Errore nella modifica del dipartimento dell\'aula.');
    }
  } catch (error) {
    document.getElementById('message').textContent = 'Errore: ' + error.message;
    document.getElementById('message').className = 'message error';
  }
});

