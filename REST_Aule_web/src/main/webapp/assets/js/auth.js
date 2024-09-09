//Funzionalità 1: Login e logout

$(document).ready(function () {
    const login = $('#login');
    const login_send = $('#login_send');
    const logout = $('#logout');

    if (!(document.cookie.indexOf('token'))) {
        login.css('display', 'none');
        logout.css('display', 'block');
        logged.css('pointer-events', 'auto');
    }

    // Send login request
    login_send.click(function () {
        const username = $('#username').val();
        const password = $('#password').val();
        $.ajax({
            url: 'rest/auth/login',
            type: 'POST',
            data: {
                username: username,
                password: password
            },
            success: function (data) {
                console.log(data);
                document.cookie = "token=" + data;
                login.css('display', 'none');
                logout.css('display', 'block');
                logged.css('pointer-events', 'auto');
            },
            error: function (request, status, error) {
                console.log(error);
            },
            cache: false,
        });
    });

    // Send logout request
    logout.click(function () {
        $.ajax({
            url: 'rest/auth/logout',
            type: 'DELETE',
            success: function () {
                // noinspection JSUnresolvedFunction
                $.removeCookie('token');
                login.css('display', 'block');
                logout.css('display', 'none');
                logged.css('pointer-events', 'none');
            },
            error: function (request, status, error) {
                alert("Errore di logout");
            },
            cache: false,
        });
    });
});