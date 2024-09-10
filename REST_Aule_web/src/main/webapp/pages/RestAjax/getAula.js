function getAula() {
  const aulaID = document.getElementById("aulaID").value;
  const url = `http://localhost:8080/REST_Aule_web/rest/aule/${aulaID}`;

  fetch(url)
    .then(response => {
      if (!response.ok) {
        throw new Error('Errore nel recupero dei dati');
      }
      return response.json();
    })
    .then(data => {
      const resultDiv = document.getElementById("result");
      resultDiv.innerHTML = `
            <h3>Dettagli Aula</h3>
            <p><strong>Nome:</strong> ${data.nome}</p>
            <p><strong>Luogo:</strong> ${data.luogo}</p>
            <p><strong>Edificio:</strong> ${data.edificio}</p>
            <p><strong>Piano:</strong> ${data.piano}</p>
            <p><strong>Capienza:</strong> ${data.capienza}</p>
            <p><strong>Prese Elettriche:</strong> ${data.preseElettriche}</p>
            <p><strong>Prese di Rete:</strong> ${data.preseDiRete}</p>
            <p><strong>Note:</strong> ${data.note}</p>
          `;
    })
    .catch(error => {
      const resultDiv = document.getElementById("result");
      resultDiv.innerHTML = `<p style="color: red;">${error.message}</p>`;
    });
}

