document.addEventListener("DOMContentLoaded", () => {
    const ordersTableBody = document.querySelector("tbody");

    // Daten von der REST-API abrufen
    fetch("/api/orders")
        .then(response => {
            if (!response.ok) {
                throw new Error("Fehler beim Laden der Bestellungen");
            }
            return response.json();
        })
        .then(orders => {
            // Tabelle mit den Bestellungen füllen
            orders.forEach(order => {
                const row = document.createElement("tr");

                row.innerHTML = `
                    <td>${order.orderId}</td>
                    <td>${order.date}</td>
                    <td>${order.total} €</td>
                    <td>${order.status}</td>
                    <td><a href="/orders/${order.orderId}" class="btn btn-primary btn-sm">Ansehen</a></td>
                `;

                ordersTableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error("Fehler:", error);
        });
});