const productId = new URLSearchParams(window.location.search).get("id");
let product = null;

if (productId) {
    fetch(`/api/products/${productId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Fehler beim Laden des Produkts.");
            }
            return response.json();
        })
        .then(data => {
            product = data; // Produkt-Objekt zuweisen
            document.getElementById("productName").textContent = product.name;
            document.getElementById("productImage").src = product.imageURL;
            document.getElementById("productDescription").textContent = product.description;
            document.getElementById("productCategory").textContent = product.category;
            document.getElementById("productPrice").textContent = product.price.toFixed(2) + " €";
            document.getElementById("productStock").textContent = product.stock;
        })
        .catch(error => {
            console.error("Fehler beim Laden des Produkts:", error);
            document.body.innerHTML = `<p class="text-danger">Produkt konnte nicht geladen werden.</p>`;
        });
} else {
    document.body.innerHTML = `<p class="text-danger">Keine Produkt-ID in der URL gefunden.</p>`;
}