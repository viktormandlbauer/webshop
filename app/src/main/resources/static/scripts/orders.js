document.addEventListener("DOMContentLoaded", async () => {
    const tbody = document.getElementById("orders-body");

    try {
        const response = await fetch("/api/orders");
        const orders = await response.json();

        orders.data.forEach(order => {
            const tr = document.createElement("tr");

            tr.innerHTML = `
                <td>${order.orderId}</td>
                <td>${order.date}</td>
                <td>€${parseFloat(order.total).toFixed(2)}</td>
                <td>${order.status}</td>
                <td>
                  <a href="/api/orders/receipt?orderId=${order.orderId}" target="_blank" class="btn btn-sm btn-primary">Details</a>
                </td>
            `;

            tbody.appendChild(tr);
        });

    } catch (error) {
        console.error("Fehler beim Laden der Bestellungen:", error);
        tbody.innerHTML = `
            <tr>
              <td colspan="5" class="text-danger">Fehler beim Laden der Bestellungen</td>
            </tr>
        `;
    }
});