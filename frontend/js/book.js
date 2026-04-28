const BOOK_STATE = {
    page: 0,
    size: 6,
    sortBy: "title",
    category: "",
    query: ""
};

async function displayAllBooks() {
    const container = document.getElementById("booksContainer");
    if (!container) {
        return;
    }

    setLoading("booksContainer", "Loading books...");
    const response = await getAllBooks(BOOK_STATE);
    const books = response.items || [];

    if (!books.length) {
        container.innerHTML = `
            <div class="empty-state">
                <h3>No books found</h3>
                <p>Try a different search, change the filter, or browse all categories.</p>
            </div>
        `;
        renderPagination(0);
        return;
    }

    container.innerHTML = books.map(book => `
        <article class="book-card">
            <img class="book-cover" src="${book.imageUrl}" alt="${book.title}">
            <div class="book-meta">
                <span class="badge">${book.category}</span>
                <span class="rating">⭐ ${book.rating} (${book.reviewCount})</span>
            </div>
            <h3>${book.title}</h3>
            <p class="muted">by ${book.author}</p>
            <p class="book-description">${book.description}</p>
            <div class="book-footer">
                <div>
                    <strong>${formatCurrency(book.price)}</strong>
                    <div class="muted">Stock: ${book.stock}</div>
                </div>
                <div class="button-row">
                    <a class="button ghost small" href="book-details.html?id=${book.id}">Details</a>
                    <button class="button small" onclick="addBookToCart('${book.id}')">Cart</button>
                    <button class="button ghost small" onclick="saveForLater('${book.id}')">Wishlist</button>
                </div>
            </div>
        </article>
    `).join("");

    renderPagination(Math.ceil((response.totalItems || 0) / BOOK_STATE.size));
}

function renderPagination(totalPages) {
    const pagination = document.getElementById("pagination");
    if (!pagination) {
        return;
    }

    if (totalPages <= 1) {
        pagination.innerHTML = "";
        return;
    }

    pagination.innerHTML = Array.from({ length: totalPages }, (_, index) => `
        <button class="page-chip ${index === BOOK_STATE.page ? "active" : ""}" onclick="goToPage(${index})">${index + 1}</button>
    `).join("");
}

function goToPage(page) {
    BOOK_STATE.page = page;
    displayAllBooks();
}

function applyBookFilters() {
    BOOK_STATE.query = document.getElementById("searchInput")?.value?.trim() || "";
    BOOK_STATE.category = document.getElementById("categoryFilter")?.value || "";
    BOOK_STATE.sortBy = document.getElementById("sortSelect")?.value || "title";
    BOOK_STATE.page = 0;
    displayAllBooks();
}

async function addBookToCart(bookId) {
    if (!isLoggedIn()) {
        showToast("Please login to add books to your cart.", "warning");
        window.location.href = "login.html";
        return;
    }

    const result = await addToCart(bookId, 1);
    if (result && result.items) {
        showToast("Book added to cart", "success");
    } else {
        showToast(result.message || "Could not add book to cart", "error");
    }
}

async function saveForLater(bookId) {
    if (!isLoggedIn()) {
        showToast("Login to use wishlist.", "warning");
        window.location.href = "login.html";
        return;
    }

    const book = await getBookById(bookId);
    if (!book) {
        showToast("Book not found", "error");
        return;
    }

    const result = await addToWishlist(book);
    if (result && result.items) {
        showToast("Saved to wishlist", "success");
    } else {
        showToast(result.message || "Could not save to wishlist", "error");
    }
}

async function displayBookDetails() {
    const container = document.getElementById("bookDetailContainer");
    if (!container) {
        return;
    }

    const params = new URLSearchParams(window.location.search);
    const bookId = params.get("id");
    if (!bookId) {
        renderPageHint("bookDetailContainer", "Book not found", "Select a book from the catalog to open its full details.");
        return;
    }

    setLoading("bookDetailContainer", "Loading book details...");
    const book = await getBookById(bookId);
    if (!book) {
        renderPageHint("bookDetailContainer", "Book not found", "The selected book could not be loaded.");
        return;
    }

    container.innerHTML = `
        <section class="detail-layout">
            <img class="detail-cover" src="${book.imageUrl}" alt="${book.title}">
            <div class="detail-content">
                <span class="badge">${book.category}</span>
                <h1>${book.title}</h1>
                <p class="muted">by ${book.author}</p>
                <p class="detail-price">${formatCurrency(book.price)}</p>
                <p>${book.description}</p>
                <div class="detail-grid">
                    <div class="detail-card"><strong>Rating</strong><span>⭐ ${book.rating}</span></div>
                    <div class="detail-card"><strong>Reviews</strong><span>${book.reviewCount}</span></div>
                    <div class="detail-card"><strong>Stock</strong><span>${book.stock}</span></div>
                </div>
                <div class="button-row">
                    <button class="button" onclick="addBookToCart('${book.id}')">Add to Cart</button>
                    <button class="button ghost" onclick="saveForLater('${book.id}')">Add to Wishlist</button>
                </div>
                <div class="review-panel">
                    <h3>Reader Notes</h3>
                    <p>Readers like this book for its clarity, engaging writing style, and practical takeaways.</p>
                    <ul>
                        <li>Great for first-time readers in this category.</li>
                        <li>Well-rated by returning customers.</li>
                        <li>Useful as a personal reading or gifting choice.</li>
                    </ul>
                </div>
            </div>
        </section>
    `;
}
