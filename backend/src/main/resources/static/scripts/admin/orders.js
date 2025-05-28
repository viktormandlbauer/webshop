// Aktuelle Seite und Anzahl der Einträge pro Seite
let currentPage = 0;
let pageSize = 10;

// Wird ausgeführt, sobald die Seite vollständig geladen ist
document.addEventListener("DOMContentLoaded", () => {
    const pageSizeSelect = document.getElementById("pageSizeSelect");
    pageSizeSelect.addEventListener("change", () => {
        pageSize = parseInt(pageSizeSelect.value);
        loadOrders(0);
    });
// Bestellungen der ersten Seite laden
    loadOrders(0);
});

async function loadOrders(page) {
    currentPage = page;
    try {
        const response = await fetch(`/api/admin/orders?page=${page}&size=${pageSize}`);
        const json = await response.json();
        const orders = json.data.content;
        const pageInfo = json.data.page;

        renderOrderTable(orders);
        renderPagination(pageInfo);
    } catch (error) {
        console.error("Fehler beim Laden der Bestellungen:", error);
        document.getElementById("order-table-body").innerHTML = `
                <tr>
                    <td colspan="4" class="text-danger">Fehler beim Laden der Bestellungen.</td>
                </tr>
            `;
    }
}

function renderOrderTable(orders) {
    const tbody = document.getElementById("order-table-body");
    tbody.innerHTML = "";

    orders.forEach(order => {
        const tr = document.createElement("tr");
        tr.classList.add("clickable-row");
        // Bei Klick auf eine Zeile: Weiterleitung zur Detailansicht
        tr.onclick = () => {
            window.location.href = `/admin/orders/view?id=${order.orderId}`;
        };

        // Tabelleninhalt für die Bestellung
        tr.innerHTML = `
                <td>${order.orderId}</td>
                <td>${order.date}</td>
                <td>€${order.total.toFixed(2)}</td>
                <td>${order.status}</td>
            `;
        tbody.appendChild(tr);
    });
}

function renderPagination(pageInfo) {
    const ul = document.getElementById("pagination-controls");
    ul.innerHTML = "";

    const current = pageInfo.number;
    const total = pageInfo.totalPages;

    const prevLi = document.createElement("li");
    prevLi.className = `page-item ${current === 0 ? "disabled" : ""}`;
    prevLi.innerHTML = `<a class="page-link" href="#">«</a>`;
    prevLi.onclick = () => {
        if (current > 0) loadOrders(current - 1);
    };
    ul.appendChild(prevLi);

    for (let i = 0; i < total; i++) {
        const li = document.createElement("li");
        li.className = `page-item ${i === current ? "active" : ""}`;
        li.innerHTML = `<a class="page-link" href="#">${i + 1}</a>`;
        li.onclick = () => loadOrders(i);
        ul.appendChild(li);
    }

    const nextLi = document.createElement("li");
    nextLi.className = `page-item ${current >= total - 1 ? "disabled" : ""}`;
    nextLi.innerHTML = `<a class="page-link" href="#">»</a>`;
    nextLi.onclick = () => {
        if (current < total - 1) loadOrders(current + 1);
    };
    ul.appendChild(nextLi);
}