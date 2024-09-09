//LISTA EVENTI PROSSIME 3 ORE
const eventi_aula_treore = $("#eventi-aula-treore");
function getEventiTreore(areaID) {
    
    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }
    
    deactivateAll();

    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    content_title.text("Lista eventi");

   markup =
        "<tr><td>Nome</td><td>Descrizione</td><td>Data</td><td>Ora di inizio</td><td>Ora di fine}</td><td>Tipologia</td><td>Persona ID</td><td>Aula ID</td><td>Corso ID</td></tr>";
    content_table_header.append(markup);

    $.ajax({
        url: "rest/collezioni/aree/" + areaID + "/prossime3ore",
        method: "GET",
        success: function (data) {
            //data - lista di url di eventi
            if (data.length > 0) {
                $.each(data, function (key) {
                    $.ajax({
                        url: "rest/eventi/" + data[key].split("/")[6],
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
                            content_body("Evento non trovato.");
                        },
                        cache: false
                    });
                });
            } else {
                content_table_header.empty();
                content_table_body.empty();
                toggleVisibility(content_body);
                content_body.text("Non ci sono eventi.");
            }
        },
        error: function (request, status, error) {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Errore nel caricamento degli eventi.");
        },
        cache: false
    });
}

//LISTA EVENTI CON AULA E SETTIMANA
const eventi_aula_settimana = $("#eventi-aula-settimana");
function getEventiSettimanale(aulaID) {
    
    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }
    
    deactivateAll();
    
    var giorno = $("#giorno-settimanale").val();
    var giornoString = new Date(giorno);
    giornoString = giornoString.getFullYear() + '-' + (giornoString.getMonth() + 1).toString().padStart(2, '0') + '-' + giornoString.getDate().toString().padStart(2, '0');


    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    content_title.text("Lista eventi settimanale");

   markup =
        "<tr><td>Nome</td><td>Descrizione</td><td>Data</td><td>Ora di inizio</td><td>Ora di fine</td><td>Tipologia</td><td>Persona ID</td><td>Aula ID</td><td>Corso ID</td></tr>";
    content_table_header.append(markup);

    $.ajax({
        url: "rest/collezioni/aule/" + aulaID + "/settimanale/" + giornoString,
        method: "GET",
        success: function (data) {
            //data - lista di url di eventi
            if (data.length > 0) {
                $.each(data, function (key) {
                    $.ajax({
                        url: "rest/eventi/" + data[key].split("/")[6],
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
            
                            content_body("Evento non trovato.");
                        },
                        cache: false
                    });
                });
            } else {
                content_table_header.empty();
                content_table_body.empty();
          
                content_body.text("Non ci sono eventi.");
            }
        },
        error: function (request, status, error) {
            content_table_header.empty();
            content_table_body.empty();
        
            content_body.text("Errore nel caricamento degli eventi.");
        },
        cache: false
    });
}

