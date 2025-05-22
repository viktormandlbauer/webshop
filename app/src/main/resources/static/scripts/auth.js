async function handleLogin(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const message = document.getElementById('login-message');

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        });

        if (!response.ok) {
            throw new Error('Invalid credentials');
        }

        const data = await response.json();

        document.cookie = `jwtToken=${data.token}; path=/;`;

        message.textContent = 'Login successful!';

        window.location.href = '/welcome';

    } catch (error) {
        message.textContent = error.message || 'Login failed. Please try again.';
        message.className = 'message error';
    }
}

async function handleRegister(event) {
    event.preventDefault();

    const salutation = document.getElementById('salutation').value;
    const firstName = document.getElementById('firstName').value;
    const lastName = document.getElementById('lastName').value;
    const dateOfBirth = document.getElementById('dateOfBirth').value;
    const country = document.getElementById('country').value;
    const address = document.getElementById('address').value;
    const postalCode = document.getElementById('postalCode').value;
    const city = document.getElementById('city').value;
    const email = document.getElementById('email').value;
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const message = document.getElementById('register-message');

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                salutation,
                firstName,
                lastName,
                dateOfBirth,
                country,
                address,
                postalCode,
                city,
                email,
                username,
                password
            }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Registrierung fehlgeschlagen.');
        }

        const data = await response.json();
        message.textContent = data.message || 'Registrierung erfolgreich! Bitte loggen Sie sich ein.';
        message.className = 'message success';
        document.getElementById('register-form').reset();
    } catch (error) {
        message.textContent = error.message || 'Registrierung fehlgeschlagen. Bitte versuchen Sie es erneut.';
        message.className = 'message error';
    }
}