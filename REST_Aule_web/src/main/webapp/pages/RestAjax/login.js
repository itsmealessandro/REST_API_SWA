function submitLogin() {
  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;

  const formData = new URLSearchParams();
  formData.append('username', username);
  formData.append('password', password);

  fetch('http://localhost:8080/REST_Aule_web/rest/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: formData.toString()
  })
    .then(response => {
      if (response.ok) {
        return response.text();
      } else {
        throw new Error('Errore durante il login');
      }
    })
    .then(data => {
      console.log(data); // Gestisci la risposta dal server (ad es., reindirizzamento)
      // Puoi aggiungere logica per il reindirizzamento o salvare un token JWT
      alert('Login avvenuto con successo!');
    })
    .catch(error => {
      document.getElementById('errorMsg').innerText = 'Credenziali errate o errore di autenticazione.';
      console.error('Errore:', error);
    });
}

