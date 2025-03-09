document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        const headers = new Headers();
        headers.append('Content-Type', 'application/json');

        // Retrieve the XSRF-Token from the cookies
        const xsrfToken = getCookie('XSRF-TOKEN');
        if (xsrfToken) {
            headers.append('X-XSRF-TOKEN', xsrfToken);
        }

        const body = JSON.stringify({ username, password });

        fetch('/login', {
            method: 'POST',
            headers: headers,
            body: body,
            credentials: 'same-origin' // Include cookies in the request
        })
            .then(response => {
                console.log(response);
                if (response.ok) {
                    window.location.href = '/';
                } else {
                    return response.text().then(text => { throw new Error(text); });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Login failed: ' + error.message);
            });
    });

    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }
});