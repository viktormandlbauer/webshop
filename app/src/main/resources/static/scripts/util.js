function decodeJWT(token) {
    try {
        // Split the token into Header, Payload, and Signature
        const [headerEncoded, payloadEncoded] = token.split('.').slice(0, 2);

        // Base64Url decode function
        const base64UrlDecode = (str) => {
            // Replace Base64Url characters (-, _) with Base64 characters (+, /)
            str = str.replace(/-/g, '+').replace(/_/g, '/');
            // Add padding if necessary
            while (str.length % 4) str += '=';
            // Decode Base64 and convert to string
            return decodeURIComponent(
                atob(str)
                    .split('')
                    .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                    .join('')
            );
        };

        // Decode and parse Header and Payload
        const header = JSON.parse(base64UrlDecode(headerEncoded));
        const payload = JSON.parse(base64UrlDecode(payloadEncoded));

        return { header, payload };
    } catch (error) {
        console.error('Invalid JWT:', error.message);
        return null;
    }
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}