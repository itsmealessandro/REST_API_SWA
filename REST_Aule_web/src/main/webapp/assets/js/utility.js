const logged = $('.logged');

const content = $('#content');
const content_body = $('#content-body-text');

const content_title = $('#content-title');
const content_table_header = $('#content-table-header');
const content_table_body = $('#content-table-body');

function toggleVisibility(elem) {
    if (elem.css('display') === 'none') {
        elem.css('display', 'block');
    } else {
        elem.css('display', 'none');
    }
}

function activate(elem) {

    if(content.css('display') === 'none') {
        toggleVisibility(content);
    }

    content_title.empty();
    content_table_body.empty();
    content_table_header.empty();

    var content_header_list = [
        'nuova-aula',
        'import-export',
        'assegna-area',
        'aula-info',
        'aula-attrezzature',
        'nuovo-evento',
        'modifica-evento',
        'evento-info',
        'eventi-aula-settimana',
        'eventi-aula-treore',
        'eventi-intervallo'
    ];

    deactivateAll();

    for (let i = 0; i < content_header_list.length; i++){
        const c = content_header_list[i];
        if (elem.attr('id') === c) elem.css('display', 'block');
    }

}

function deactivateAll() {

    var content_header_list = [
        'content-body-text',
        'nuova-aula',
        'import-export',
        'assegna-area',
        'aula-info',
        'aula-attrezzature',
        'nuovo-evento',
        'modifica-evento',
        'evento-info',
        'eventi-aula-settimana',
        'eventi-aula-treore',
        'eventi-intervallo'
    ];

    for (let i = 0; i < content_header_list.length; i++){
        const c = $('#' + content_header_list[i]);
        c.css('display', 'none');
    }

}