
let currentPage = 0;
const pageSize = 3;
let currentCategory = '';

//load Categories
function fetchCategories() {
    fetch('/api/categories')
        .then(r => r.json())
        .then(list => {
            const select = document.getElementById('category');
            select.innerHTML = '<option value="">Alle Kategorien</option>';
            const seen = new Set();
            list.forEach(cat => {
                if (seen.has(cat.name)) return;
                seen.add(cat.name);

                const opt = document.createElement('option');
                opt.value = cat.name;
                opt.textContent = cat.name;
                select.appendChild(opt);
            });
        })
        .catch(err => console.error('Kategorie‑Load‑Fehler:', err));
}

//Products rendering
function fetchProducts(page) {
    const productGrid = document.getElementById('product-grid');
    const paginationControls = document.getElementById('pagination-controls');
    productGrid.innerHTML = '<p>Lade Produkte …</p>';

    const params = new URLSearchParams({ page, size: pageSize });
    if (currentCategory) params.append('category', currentCategory);

    fetch(`/api/products?${params.toString()}`)
        .then(r => { if (!r.ok) throw new Error('Fetch‑Fehler'); return r.json(); })
        .then(data => {
            productGrid.innerHTML = '';

            if (!Array.isArray(data.content) || data.content.length === 0) {
                productGrid.innerHTML = '<p class="text-muted">Keine Produkte gefunden.</p>';
                paginationControls.innerHTML = '';
                return;
            }

            data.content.forEach(product => {
                const col = document.createElement('div');
                col.className = 'col-md-4 mb-4';
                col.innerHTML = `
                   <div class="card h-100">
                       <img src="${product.imageURL || '/images/ProductPlaceholder.jpeg'}"
                            onerror="this.src='/images/ProductPlaceholder.jpeg'"
                            class="card-img-top"
                            style="height:200px;object-fit:cover;" alt="${product.name}">
                       <div class="card-body">
                           <h5 class="card-title">${product.name}</h5>
                           <p class="card-text">${product.description}</p>
                           <p class="card-text"><strong>Kategorie:</strong> ${product.category.name}</p>
                           <p class="card-text"><strong>Preis:</strong> €${product.price.toFixed(2)}</p>
                           <p class="card-text"><strong>Bewertung:</strong> ${product.avgRating.toFixed(2)} / 5</p>
                           <a href="/products/${product.id}" class="btn btn-primary">Details</a>
                       </div>
                   </div>`;
                productGrid.appendChild(col);
            });

            // Pagination
            const pageNum = Number(data.number);
            const pageCount = Number(data.totalPages);

            if (Number.isFinite(pageNum) && Number.isFinite(pageCount) && pageCount >= 1) {
                currentPage = pageNum;

                paginationControls.innerHTML = `
                  <nav aria-label="Product pagination">
                    <ul class="pagination">
                      <li class="page-item ${data.first ? 'disabled' : ''}">
                        <button type="button" class="page-link"
                                onclick="fetchProducts(${pageNum - 1})"
                                ${data.first ? 'disabled' : ''}>Zurück</button>
                      </li>

                      <li class="page-item disabled">
                        <span class="page-link">
                          Seite ${pageNum + 1} von ${pageCount}
                        </span>
                      </li>

                      <li class="page-item ${data.last ? 'disabled' : ''}">
                        <button type="button" class="page-link"
                                onclick="fetchProducts(${pageNum + 1})"
                                ${data.last ? 'disabled' : ''}>Weiter</button>
                      </li>
                    </ul>
                  </nav>`;
            } else {
                paginationControls.innerHTML = '';
            }
        })
        .catch(err => {
            console.error(err);
            productGrid.innerHTML = '<p class="text-danger">Produkte konnten nicht geladen werden.</p>';
            paginationControls.innerHTML = '';
        });
}


document.addEventListener('DOMContentLoaded', () => {
    fetchCategories();
    fetchProducts(currentPage);

    document.getElementById('category-form').addEventListener('submit', e => {
        e.preventDefault();
        currentCategory = document.getElementById('category').value;
        currentPage     = 0;
        fetchProducts(currentPage);
    });
});
