function loadProfile() {
    fetch("/api/profile")
        .then(response => {
            if (!response.ok) {
                console.error("HTTP-Fehler:", response.status, response.statusText);
                throw new Error("Fehler beim Laden des Profils");
            }
            return response.json();
        })
        .then(data => {
            console.log("Empfangene Daten:", data);
            document.getElementById("salutation").textContent = data.salutation || "N/A";
            document.getElementById("firstName").textContent = data.firstName || "N/A";
            document.getElementById("lastName").textContent = data.lastName || "N/A";
            document.getElementById("country").textContent = data.country || "N/A";
            document.getElementById("address").textContent = data.address || "N/A";
            document.getElementById("postalCode").textContent = data.postalCode || "N/A";
            document.getElementById("city").textContent = data.city || "N/A";
            document.getElementById("email").textContent = data.email || "N/A";
            document.getElementById("username").textContent = data.username || "N/A";
            document.getElementById("dateOfBirth").textContent = data.dateOfBirth || "N/A";
        })
        .catch(error => {
            console.error("Fehler beim Laden des Profils:", error);
            alert("Profil konnte nicht geladen werden.");
        });
}

document.addEventListener("DOMContentLoaded", () => {
    loadProfile();
});