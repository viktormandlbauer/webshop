let currentPage = 0;
const pageSize = 6;
let selectedMinRating = 0;
let isFilterActive = false;
let lastFilterParams = '';

/**
 * Rendert interaktive Sterne zur Auswahl einer Mindestbewertung im Filterbereich.
 * Nutzer können auf einen Stern klicken, um z.B.: "mindestens 3 Sterne" zu filtern.
 * Beim Hover wird eine Vorschau angezeigt, die sich nach dem Klick dauerhaft übernimmt.
 */
function renderInteractiveRatingFilter() {
    const container = document.getElementById('ratingStars');
    container.innerHTML = ''; // Vorherige Sterne entfernen

    for (let i = 1; i <= 5; i++) {
        const star = document.createElement('i');
        star.classList.add('fa-star', 'fa-xl', 'me-1');
        star.dataset.value = i;
        star.style.cursor = 'pointer';

        // Aktuellen Bewertungsstatus anzeigen (z.B.: 3 von 5 aktiv)
        if (i <= selectedMinRating) {
            star.classList.add('fas', 'text-warning'); // gefüllter Stern
        } else {
            star.classList.add('far', 'text-muted'); // leerer Stern
        }

        // Zeige Vorschau beim Überfahren mit der Maus
        star.addEventListener('mouseenter', () => {
            updateStars(i);
        });

        // Übernehme Auswahl beim Klick
        star.addEventListener('click', () => {
            selectedMinRating = i;
            renderInteractiveRatingFilter(); // neu rendern mit festgelegtem Wert
        });

        // Vorschau zurücksetzen, wenn Maus das Element verlässt
        star.addEventListener('mouseleave', () => {
            updateStars(selectedMinRating);
        });

        container.appendChild(star);
    }

    /**
     * Aktualisiert die Darstellung der Sterne abhängig vom übergebenen Wert.
     * Wird verwendet für Hover-Vorschau und zum Zurücksetzen.
     */
    function updateStars(highlight) {
        const stars = container.querySelectorAll('i');
        stars.forEach((s, index) => {
            const value = index + 1;
            s.className = '';
            s.classList.add('fa-star', 'fa-xl', 'me-1');
            if (value <= highlight) {
                s.classList.add('fas', 'text-warning');
            } else {
                s.classList.add('far', 'text-muted');
            }
        });
    }
}

/**
 * Zeigt eine Bewertung (z.B.: 3.5) als statische Sternanzeige an.
 * Wird auf Produktkarten verwendet, nicht interaktiv.
 *
 * Bewertungslogik: Auf halbe Sterne runden und entsprechend darstellen.
 * Beispiel: 4.3 → 4 volle, 1 halber Stern
 */
function renderStaticRatingStars(rating) {
    const rounded = Math.round(rating * 2) / 2;
    let starsHtml = '';

    for (let i = 1; i <= 5; i++) {
        if (rounded >= i) {
            starsHtml += '<i class="fas fa-star text-warning"></i>'; // voller Stern
        } else if (rounded >= i - 0.5) {
            starsHtml += '<i class="fas fa-star-half-alt text-warning"></i>'; // halber Stern
        } else {
            starsHtml += '<i class="far fa-star text-muted"></i>'; // leerer Stern
        }
    }
    return `${starsHtml} <span class="text-secondary small">(${rating.toFixed(2)})</span>`;
}

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
                <p class="card-text rating-stars">${renderStaticRatingStars(product.avgRating ?? 0)}</p>
                <a href="/products/view?id=${product.id}" class="btn btn-primary">View Details</a>
            </div>
        </div>
    `;
    return card;
}

function renderPagination(currentPage, totalPages, isFirst, isLast) {
    const paginationControls = document.getElementById('pagination-controls');
    paginationControls.innerHTML = `
        <nav aria-label="Product pagination">
            <ul class="pagination">
                <li class="page-item ${isFirst ? 'disabled' : ''}">
                    <button class="page-link" ${isFirst ? 'disabled' : ''} onclick="fetchProducts(${currentPage - 1})">Previous</button>
                </li>
                <li class="page-item disabled">
                    <span class="page-link">Page ${currentPage + 1} of ${totalPages}</span>
                </li>
                <li class="page-item ${isLast ? 'disabled' : ''}">
                    <button class="page-link" ${isLast ? 'disabled' : ''} onclick="fetchProducts(${currentPage + 1})">Next</button>
                </li>
            </ul>
        </nav>
    `;
}


/**
 * Lädt Produkte seitenweise über das Backend.
 * Wenn ein Filter aktiv ist, wird stattdessen die Filterfunktion aufgerufen.
 * Ansonsten werden alle Produkte ohne Einschränkung geladen.
 */
function fetchProducts(page) {
    // Wenn aktuell ein Filter aktiv ist, leite direkt an applyFilters() weiter
    if (isFilterActive) {
        const params = new URLSearchParams(lastFilterParams); // Vorherige Filterparameter erneut verwenden
        params.set('page', page); // Aktuelle Seite setzen
        return applyFilters(page, lastFilterParams); // Gefilterte Ergebnisse mit Paging laden
    }

    // Standard-Fall: Alle Produkte ohne Filter laden
    const productGrid = document.getElementById('product-grid');
    const paginationControls = document.getElementById('pagination-controls');

    // Ladehinweis anzeigen
    productGrid.innerHTML = '<p>Loading products...</p>';

    // API-Aufruf an /products/all mit Paging-Parametern
    fetch(`/api/products/all?page=${page}&size=${pageSize}`)
        .then(response => {
            // Fehler abfangen, falls die Antwort vom Server nicht erfolgreich war
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json(); // JSON-Daten auslesen
        })
        .then(data => {
            // Aktuelle Seite und Gesamtanzahl berechnen
            currentPage = data?.page.number ?? 0;
            const totalPages = data?.page.totalPages ?? 1;

            // Ungültige Seiten (z.B.: Seite 3 bei nur 2 Seiten) abbrechen
            if (currentPage < 0 || currentPage >= totalPages) return;

            // Produktliste leeren und neue Inhalte einfügen
            productGrid.innerHTML = '';
            data.content.forEach(product => {
                productGrid.appendChild(renderProductCard(product));
            });

            // Blätterfunktion anzeigen (z.B.: „Seite 1 von 3“)
            renderPagination(currentPage, totalPages, data.page.first, data.page.last);
        })
        .catch(error => {
            // Fehler behandeln, z.B.: wenn die API nicht erreichbar ist
            console.error('Error fetching products:', error);
            productGrid.innerHTML = '<p class="text-danger">Failed to load products. Please try again later.</p>';
            paginationControls.innerHTML = '';
        });
}



/**
 * Filtert die Produktliste anhand ausgewählter Kriterien (Kategorie, Preis, Bewertung).
 * Wird auch für Pagination im Filtermodus verwendet.
 *
 * @param {number} page - Die anzuzeigende Seite (0-basiert)
 * @param {string|URLSearchParams|null} externalParams - Optional: Filterparameter z.B.: aus vorheriger Session
 */
async function applyFilters(page = 0, externalParams = null) {
    // Wenn externe Parameter übergeben wurden, verwenden wir diese (z.B.: bei "Next"-Klick)
    // Ansonsten holen wir die aktuellen Werte aus den Filter-UI-Elementen
    const params = externalParams ? new URLSearchParams(externalParams) : new URLSearchParams();

    if (!externalParams) {
        // Kategorie-Filter ermitteln (z.B.: "Elektronik")
        const selectedCategory = document.querySelector('input[name="category"]:checked')?.value;
        if (selectedCategory) params.append('categoryName', selectedCategory);

        // Preisfilter übernehmen, falls gesetzt
        const minPrice = document.getElementById('minPrice').value;
        const maxPrice = document.getElementById('maxPrice').value;
        if (minPrice) params.append('minPrice', minPrice);
        if (maxPrice) params.append('maxPrice', maxPrice);

        // Bewertungsfilter übernehmen (z.B.: mindestens 4 Sterne)
        if (selectedMinRating > 0) {
            params.append('minRating', selectedMinRating);
        }
    }

    // Paging-Parameter setzen
    params.set('page', page);
    params.set('size', pageSize);

    // Merke, dass der Filtermodus aktiv ist (wichtig für die Navigation)
    isFilterActive = true;

    // Die aktuelle Filterkonfiguration speichern, um bei Pagination wiederverwenden zu können
    lastFilterParams = new URLSearchParams(params).toString();

    // Ladeanzeige im UI setzen
    const productGrid = document.getElementById('product-grid');
    const paginationControls = document.getElementById('pagination-controls');
    productGrid.innerHTML = '<p>Produkte werden geladen...</p>';
    paginationControls.innerHTML = '';

    try {
        // Filterabfrage an das Backend senden
        const response = await fetch(`/api/products/search/advanced?${params.toString()}`);
        if (!response.ok) throw new Error('Fehler beim Abrufen der Produkte');

        const data = await response.json();
        productGrid.innerHTML = '';

        // Kein Treffer: entsprechende Info anzeigen
        if (data.content.length === 0) {
            productGrid.innerHTML = '<p>Keine Produkte gefunden.</p>';
            return;
        }

        // Produkte in das Grid einfügen
        data.content.forEach(product => {
            productGrid.appendChild(renderProductCard(product));
        });

        // Aktuelle Seite merken und Pagination anzeigen
        currentPage = data.page.number ?? 0;
        renderPagination(currentPage, data.page.totalPages ?? 1, data.page.first, data.page.last);
    } catch (error) {
        // Fehlerbehandlung bei Serverproblemen oder ungültigen Parametern
        console.error('Filterfehler:', error);
        productGrid.innerHTML = '<p class="text-danger">Fehler beim Laden der Filterergebnisse.</p>';
    }
}


/**
 * Setzt alle Filter zurück und lädt die vollständige Produktliste.
 * Wird über den Button "Zurücksetzen" im Filterbereich ausgelöst.
 */
function resetFilters() {
    // Eingabefelder zurücksetzen
    document.getElementById('minPrice').value = '';
    document.getElementById('maxPrice').value = '';
    selectedMinRating = 0;
    isFilterActive = false;
    lastFilterParams = '';

    // Kategorieauswahl zurücksetzen
    const checked = document.querySelector('input[name="category"]:checked');
    if (checked) checked.checked = false;

    // Bewertungssterne neu anzeigen (alle leer)
    renderInteractiveRatingFilter();

    // Ungefilterte Produktliste laden
    fetchProducts(0);
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

// Erstellt Checkboxen für jede Kategorie im Filterbereich
async function loadCategoryCheckboxes() {
    const container = document.getElementById('categoryCheckboxes');
    if (!container) return;

    try {
        const response = await fetch('/api/categories');
        const categories = await response.json();

        categories.forEach(category => {
            const wrapper = document.createElement('div');
            wrapper.className = 'form-check';

            wrapper.innerHTML = `
                <input class="form-check-input" type="radio" value="${category.name}" id="cat-${category.id}" name="category">
                <label class="form-check-label" for="cat-${category.id}">
                    ${category.name}
                </label>
            `;

            container.appendChild(wrapper);
        });
    } catch (error) {
        console.error('Fehler beim Laden der Kategorien:', error);
        container.innerHTML = '<p class="text-danger">Kategorien konnten nicht geladen werden.</p>';
    }
}

// Initialisiert die Seite
document.addEventListener('DOMContentLoaded', () => {
    loadCategories();
    loadCategoryCheckboxes();
    renderInteractiveRatingFilter()

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

    // Filterbutton mit Event verknüpfen
    document.getElementById('applyFilters')?.addEventListener('click', () => applyFilters(0));
    // Reset-Filterbutton mit Event verknüpfen
    document.getElementById('resetFilters')?.addEventListener('click', resetFilters);

});
