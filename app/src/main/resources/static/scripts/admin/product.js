let currentPage = 0;
let pageSize = 10;

document.addEventListener("DOMContentLoaded", () => {
    const pageSizeSelect = document.getElementById("pageSizeSelect");
    pageSizeSelect.addEventListener("change", () => {
        pageSize = parseInt(pageSizeSelect.value);
        loadProducts(0);
    });

    loadProducts(0);
});

async function loadProducts(page) {
    currentPage = page;
    try {
        const response = await fetch(`/api/admin/products?page=${page}&size=${pageSize}`);
        const json = await response.json();
        const products = json.data.content;
        const pageInfo = json.data.page;

        renderProductTable(products);
        renderPagination(pageInfo);
    } catch (error) {
        console.error("Fehler beim Laden der Produkte:", error);
        document.getElementById("product-table-body").innerHTML = `
                <tr><td colspan="5" class="text-danger">Fehler beim Laden der Produktdaten.</td></tr>
            `;
    }
}

function renderProductTable(products) {
    const tbody = document.getElementById("product-table-body");
    tbody.innerHTML = "";

    products.forEach(product => {
        const tr = document.createElement("tr");
        tr.classList.add("clickable-row");
        tr.onclick = () => {
            window.location.href = `/admin/products/view?id=${product.productId}`;
        };
        tr.innerHTML = `
                <td>${product.productId}</td>
                <td>${product.name}</td>
                <td>${product.category}</td>
                <td>€${product.price.toFixed(2)}</td>
                <td>${product.stock}</td>
            `;
        tbody.appendChild(tr);
    });

    // Letzte Zeile: "Produkt hinzufügen"-Button über alle Spalten
    const addRow = document.createElement("tr");
    addRow.innerHTML = `
            <td colspan="5" class="text-center">
                <a href="/admin/products/add" class="btn btn-primary">+ Produkt hinzufügen</a>
            </td>
        `;
    tbody.appendChild(addRow);
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
        if (current > 0) loadProducts(current - 1);
    };
    ul.appendChild(prevLi);

    for (let i = 0; i < total; i++) {
        const li = document.createElement("li");
        li.className = `page-item ${i === current ? "active" : ""}`;
        li.innerHTML = `<a class="page-link" href="#">${i + 1}</a>`;
        li.onclick = () => loadProducts(i);
        ul.appendChild(li);
    }

    const nextLi = document.createElement("li");
    nextLi.className = `page-item ${current >= total - 1 ? "disabled" : ""}`;
    nextLi.innerHTML = `<a class="page-link" href="#">»</a>`;
    nextLi.onclick = () => {
        if (current < total - 1) loadProducts(current + 1);
    };
    ul.appendChild(nextLi);
}