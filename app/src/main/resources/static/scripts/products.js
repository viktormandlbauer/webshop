let currentPage = 0;
const pageSize = 6;

// Rendert eine einzelne Produktkarte und gibt das DOM-Element zurück
function renderProductCard(product) {
    const card = document.createElement('div');
    card.className = 'col-md-4 mb-4';
    card.innerHTML = `
        <div class="card h-100">
            <img src="${product.imageURL}" class="card-img-top" alt="${product.name}" style="height: 200px; object-fit: cover;">
            <div class="card-body">
                <h5 class="card-title">${product.name}</h5>
                <p class="card-text">${product.description}</p>
                <p class="card-text"><strong>Category:</strong> ${product.categoryName}</p>
                <p class="card-text"><strong>Price:</strong> $${product.price.toFixed(2)}</p>
                <p class="card-text"><strong>Stock:</strong> ${product.stock}</p>
                <p class="card-text"><strong>Rating:</strong> ${(product.avgRating ?? 0).toFixed(2)} / 5</p>
                <a href="/products/view?id=${product.id}" class="btn btn-primary">View Details</a>
            </div>
        </div>
    `;
    return card;
}

// Lädt Produkte seitenweise über das Backend
function fetchProducts(page) {
    const productGrid = document.getElementById('product-grid');
    const paginationControls = document.getElementById('pagination-controls');

    productGrid.innerHTML = '<p>Loading products...</p>';

    fetch(`/api/products/all?page=${page}&size=${pageSize}`)
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            productGrid.innerHTML = '';
            data.content.forEach(product => {
                productGrid.appendChild(renderProductCard(product));
            });

            currentPage = data?.number ?? 0;
            const totalPages = data?.totalPages ?? 1;

            paginationControls.innerHTML = `
                <nav aria-label="Product pagination">
                    <ul class="pagination">
                        <li class="page-item ${data.first ? 'disabled' : ''}">
                            <button class="page-link" onclick="fetchProducts(${currentPage - 1})" ${data.first ? 'disabled' : ''}>Previous</button>
                        </li>
                        <li class="page-item disabled">
                            <span class="page-link">Page ${currentPage + 1} of ${totalPages}</span>
                        </li>
                        <li class="page-item ${data.last ? 'disabled' : ''}">
                            <button class="page-link" onclick="fetchProducts(${currentPage + 1})" ${data.last ? 'disabled' : ''}>Next</button>
                        </li>
                    </ul>
                </nav>
            `;
        })
        .catch(error => {
            console.error('Error fetching products:', error);
            productGrid.innerHTML = '<p class="text-danger">Failed to load products. Please try again later.</p>';
            paginationControls.innerHTML = '';
        });
}

// Durchsucht Produkte anhand eines Suchbegriffs
function searchProducts(query) {
    const productGrid = document.getElementById('product-grid');
    const paginationControls = document.getElementById('pagination-controls');

    if (!query) {
        fetchProducts(currentPage);
        return;
    }

    productGrid.innerHTML = '<p>Suche läuft...</p>';
    paginationControls.innerHTML = '';

    fetch(`/api/products/search?q=${encodeURIComponent(query)}`)
        .then(response => {
            if (!response.ok) throw new Error('Fehler bei der Produktsuche');
            return response.json();
        })
        .then(products => {
            productGrid.innerHTML = '';

            if (products.length === 0) {
                productGrid.innerHTML = '<p>Keine Produkte gefunden.</p>';
                return;
            }

            products.forEach(product => {
                productGrid.appendChild(renderProductCard(product));
            });
        })
        .catch(error => {
            console.error('Fehler bei der Suche:', error);
            productGrid.innerHTML = '<p class="text-danger">Fehler beim Suchen. Bitte versuche es später erneut.</p>';
        });
}

// Sendet das Produktformular (Add Product)
// Sendet das Produktformular (Add Product) mit Dropzone-Integration
async function handleProductUpload(event) {
    event.preventDefault();

    const form = document.getElementById('product-form');
    const formData = new FormData(form);
    const message = document.getElementById('product-message');
    const dropzone = Dropzone.forElement("div.my-dropzone");

    try {
        // Überprüfen, ob eine Datei in der Dropzone vorhanden ist
        if (dropzone.getQueuedFiles().length > 0) {
            // Füge die Datei aus der Dropzone zum FormData hinzu
            dropzone.getQueuedFiles().forEach(file => {
                formData.append("imageFile", file);
            });
        } else {
            throw new Error("Bitte ein Bild hochladen.");
        }

        const response = await fetch('/api/admin/products/add', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Fehler beim Speichern');
        }

        message.textContent = 'Produkt erfolgreich hinzugefügt!';
        message.className = 'message success';
        form.reset();
        dropzone.removeAllFiles(); // Entfernt die Dateien aus der Dropzone
    } catch (error) {
        message.textContent = error.message || 'Fehler beim Hochladen';
        message.className = 'message error';
    }
}

// Lädt alle verfügbaren Kategorien beim Seitenstart
async function loadCategories() {
    const categorySelect = document.getElementById('productCategory');
    if (!categorySelect) return;

    try {
        const response = await fetch('/api/categories');
        const categories = await response.json();

        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.id;
            option.textContent = category.name;
            categorySelect.appendChild(option);
        });
    } catch (error) {
        console.error('Kategorien konnten nicht geladen werden:', error);
    }
}

// Initialisiert die Seite
document.addEventListener('DOMContentLoaded', () => {
    loadCategories();

    const productGrid = document.getElementById('product-grid');
    if (productGrid) {
        fetchProducts(currentPage);
    }

    const searchInput = document.getElementById('productSearchInput');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            searchProducts(e.target.value);
        });
    }
});
