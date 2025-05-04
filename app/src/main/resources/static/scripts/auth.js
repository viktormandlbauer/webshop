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

    } catch (error) {
        message.textContent = error.message || 'Login failed. Please try again.';
        message.className = 'message error';
    }
}

async function handleRegister(event) {
    event.preventDefault();
    const username = document.getElementById('register-username').value;
    const password = document.getElementById('register-password').value;
    const message = document.getElementById('register-message');

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        });

        if (!response.ok) {
            throw new Error('Registration failed. Username may already exist.');
        }

        const data = await response.json();
        message.textContent = data.message || 'Registration successful! Please login.';
        message.className = 'message success';
        document.getElementById('register-form').reset();

        showLogin();
    } catch (error) {
        message.textContent = error.message || 'Registration failed. Please try again.';
        message.className = 'message error';
    }
}