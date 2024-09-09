//CREA EVENTO
function creaEvento() {
    
    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }
    
    deactivateAll();
    
    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    deactivateAll();

    $.ajax({
        url: "rest/eventi/crea",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            nome: $('#nome2').val(),
            descrizione: $('#descrizione').val(),
            data: $('#data').val(),
            oraInizio: $('#oraInizio').val(),
            oraFine: $('#oraFine').val(),
            tipologiaEvento: $('#tipologiaEvento').val(),
            personaID: $('#personaID').val(),
            aulaID: $('#aulaID6').val(),
            corsoID: $('#corsoID').val()
        }),
        success: function () {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Evento inserito");
        },
        error: function (request, status, error) {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Errore nell'inserimento.");
        },
        cache: false
    });
}

//MODIFICA EVENTO
function modificaEvento(eventoID) {
    
    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }
    
    deactivateAll();
        
    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    deactivateAll();

    $.ajax({
        url: "rest/eventi/" + eventoID + "/modificaEvento",
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify({
            nome: $('#nome3').val(),
            descrizione: $('#descrizione2').val(),
            data: $('#data10').val(),
            oraInizio: $('#oraInizio2').val(),
            oraFine: $('#oraFine2').val(),
            tipologiaEvento: $('#tipologiaEvento2').val(),
            personaID: $('#personaID2').val(),
            aulaID: $('#aulaID7').val(),
            corsoID: $('#corsoID2').val(),
        }),
        success: function () {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Evento aggiornato");
        },
        error: function (request, status, error) {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Errore nell'aggiornamento.");
        },
        cache: false
    });
}

//INFO EVENTO
const evento_info = $('#evento-info');
function getEvento(eventoID) {
    
    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }
    
    deactivateAll();

    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    content_title.text("Informazioni evento");

    markup =
        "<tr><td>Nome</td><td>Descrizione</td><td>Data</td><td>Ora di inizio</td><td>Ora di fine}</td><td>Tipologia</td><td>Persona ID</td><td>Aula ID</td><td>Corso ID</td></tr>";
    content_table_header.append(markup);

    $.ajax({
        url: "rest/eventi/" + eventoID,
        method: "GET",
        success: function (data) {
            markup =
                "<tr>" +
                "<td>" + data["nome"] + "</td>" +
                "<td>" + data["descrizione"] + "</td>" +
                "<td>" + data["data"] + "</td>" +
                "<td>" + data["oraInizio"] + "</td>" +
                "<td>" + data["oraFine"] + "</td>" +
                "<td>" + data["tipologiaEvento"] + "</td>" +
                "<td>" + data["personaId"] + "</td>" +
                "<td>" + data["aulaId"] + "</td>" +
                "<td>" + data["corsoId"] + "</td>" +
                "</tr>";
            content_table_body.append(markup);
        },
        error: function (request, status, error) {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Evento non trovato.");
        }
    });
}

//Esportazione di tutti gli eventi relativi a un certo intervallo di tempo
function getEventiIntervallo() {
    
    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }
    
    deactivateAll();
    
    var dataI = $("#dataInizio").val();
    var dataF = $("#dataFine").val();
    var dataIS = new Date(dataI);
    var dataFS = new Date(dataF);
    dataIS = dataIS.getFullYear() + '-' + (dataIS.getMonth() + 1).toString().padStart(2, '0') + '-' + dataIS.getDate().toString().padStart(2, '0');
    dataFS = dataFS.getFullYear() + '-' + (dataFS.getMonth() + 1).toString().padStart(2, '0') + '-' + dataFS.getDate().toString().padStart(2, '0');

    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    content_title.text("Esportazione eventi");

    $.ajax({
        url: "rest/eventi/esportazione/" + dataIS + "/" + dataFS,
        method: "GET",
        contentType: "application/json",
        xhrFields: {
        responseType: 'blob' // Imposta il tipo di risposta come 'blob'
        },
        success: function (fileCSV) {
          var url = window.URL.createObjectURL(fileCSV);
            var a = document.createElement('a');
            a.href = url;
            a.download = 'eventi.csv'; // Nome del file da scaricare
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            window.URL.revokeObjectURL(url);
        },
        error: function (request, status, error) {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Esportazione fallita");
        }
    });
}
