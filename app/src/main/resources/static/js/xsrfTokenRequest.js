async function fetchCsrfToken() {
    const response = await fetch('/csrf-token', {
        method: 'GET',
        credentials: 'include'
    });
    if (!response.ok) {
        throw new Error('Failed to fetch CSRF token');
    }
    const data = await response.json();
    return {
        headerName: data.headerName,
        token: data.token
    };
}

async function makeSecureRequest(url, data) {
    try {
        const csrfData = await fetchCsrfToken();
        if (!csrfData.token) {
            throw new Error('CSRF token not available');
        }

        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfData.headerName]: csrfData.token
            },
            body: JSON.stringify(data),
            credentials: 'include'
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('Request failed:', error);
        throw error;
    }
}
