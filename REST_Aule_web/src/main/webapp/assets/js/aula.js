//Inserimento di una nuova aula
function creaAula() {
    
    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }
    
    deactivateAll();
    
    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    $.ajax({
        url: "rest/aule/crea",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            nome: $('#nome').val(),
            note: $('#note').val(),
            edificio: $('#edificio').val(),
            piano: $('#piano').val(),
            zona: $('#zona').val(),
            capienza: $('#capienza').val(),
            preseElettriche: $('#preseElettriche').val(),
            preseDiRete: $('#preseDiRete').val(),
            attrezzature: $('#attrezzature').val(),
            areaID: $('#areaID').val()
        }),
        success: function () {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Aula inserita");            
        },
        error: function (request, status, error) {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Errore nell'inserimento");
        },
        cache: false,
    });
}
//Assegnazione di un'aula a un gruppo
function aulaArea(aulaID, areaID){
    
    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }
    
    deactivateAll();
    
    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    $.ajax({
        url: "rest/aule/"+ aulaID +"/assegnaArea",
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify({
            areaId: areaID
        }),
        success: function () {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Area assegnata correttamente");            
        },
        error: function (request, status, error) {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Errore nell'assegnazione");
        },
        cache: false,
    });
}
//Informazioni di base relative a un'aula
const aula_info = $('#aula-info');
function getAula(aula) {
    
    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }
    
    deactivateAll();

    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    content_title.text("Informazioni su un'aula");

    markup = "<tr><th>Nome</th><th>Note</th><th>Edificio</th><th>Piano</th><th>Zona</th><th>Capienza</th><th>Prese elettriche</th><th>Prese di rete</th><th>Attrezzature</th><th>Area ID</th></tr>";
    content_table_header.append(markup);

    $.ajax({
        url: "rest/aule/" + aula,
        method: "GET",
        success: function (data) {
            markup = "<tr>" +
                "<td>" + data['nome'] + "</td>" +
                "<td>" + data['note'] + "</td>" +
                "<td>" + data['edificio'] + "</td>" +
                "<td>" + data['piano'] + "</td>" +
                "<td>" + data['zona'] + "</td>" +
                "<td>" + data['capienza'] + "</td>" +
                "<td>" + data['preseElettriche'] + "</td>" +
                "<td>" + data['preseDiRete'] + "</td>" +
                "<td>" + data['attrezzature'] + "</td>" +
                "<td>" + data['areaId'] + "</td>" +
                "</tr>";
            content_table_body.append(markup);
        },
        error: function (request, status, error) {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Aula non trovata.");
        },
        cache: false,
    });
}
//Lista delle attrezzature presenti in un'aula
function getAttrezzature(aula) {
    
    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }
    
    deactivateAll();

    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    content_title.text("Lista attrezzature di un'aula");

    markup = "<tr><th>Attrezzature</th></tr>";
    content_table_header.append(markup);

    $.ajax({
        url: "rest/aule/" + aula + "/attrezzature",
        method: "GET",
        success: function (data) {
            markup = "<tr>" +
                "<td>" + data['attrezzature'] + "</td>" +
                "</tr>";
            content_table_body.append(markup);
        },
        error: function (request, status, error) {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Attrezzatura non trovata.");
        },
        cache: false,
    });
}
//Importazione CSV configurazione aule
function importCSV() {
    
    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }
    
    deactivateAll();

    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    content_title.text("Importazione configurazione di un'aula");
    
    var fileInput = document.getElementById('csvFile');
    var file = fileInput.files[0];
    var formData = new FormData();
    formData.append('csvFile', file);

    $.ajax({
        url: "rest/aule/importazione",
        method: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function () {
            content_table_body.text("Importato con successo");
        },
        error: function (request, status, error) {
            content_table_header.empty();
            content_table_body.empty();
            toggleVisibility(content_body);
            content_body.text("Importazione fallita");
        }
    });
}
//Esportazione CSV configurazione aule
function exportCSV() {
    
    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }
    
    deactivateAll();

    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    content_title.text("Esportazione configurazione di un'aula");

    $.ajax({
        url: "rest/aule/esportazione",
        method: "GET",
        xhrFields: {
        responseType: 'blob' // Imposta il tipo di risposta come 'blob'
        },
        success: function (fileCSV) {
          var url = window.URL.createObjectURL(fileCSV);
            var a = document.createElement('a');
            a.href = url;
            a.download = 'aula.csv'; // Nome del file da scaricare
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
        },
        cache: false,
    });
}