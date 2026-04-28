const API_URL = "http://localhost:8080/api";

function getToken() {
    return localStorage.getItem("token");
}

function isLoggedIn() {
    return !!localStorage.getItem("token");
}

function getCurrentUser() {
    return {
        userId: localStorage.getItem("userId"),
        name: localStorage.getItem("userName"),
        role: localStorage.getItem("userRole")
    };
}

function logout() {
    clearSession();
    window.location.href = "index.html";
}

async function request(path, options = {}) {
    const config = {
        method: options.method || "GET",
        headers: {
            "Content-Type": "application/json",
            ...(options.auth ? { "Authorization": `Bearer ${getToken()}` } : {}),
            ...(options.headers || {})
        }
    };

    if (options.body !== undefined) {
        config.body = JSON.stringify(options.body);
    }

    const response = await fetch(`${API_URL}${path}`, config);
    const text = await response.text();
    const data = text ? JSON.parse(text) : null;

    if (response.status === 401) {
        clearSession();
        if (getCurrentPage() !== "login.html") {
            showToast("Please login to continue.", "warning");
        }
    }

    if (!response.ok) {
        throw new Error(data?.message || "Request failed");
    }

    return data;
}

async function register(name, email, password) {
    try {
        return await request("/auth/register", {
            method: "POST",
            body: { name, email, password }
        });
    } catch (error) {
        return { success: false, message: error.message || "Registration failed" };
    }
}

async function login(email, password) {
    try {
        return await request("/auth/login", {
            method: "POST",
            body: { email, password }
        });
    } catch (error) {
        return { success: false, message: error.message || "Login failed" };
    }
}

async function getAllBooks(params = {}) {
    const search = new URLSearchParams({
        page: params.page ?? 0,
        size: params.size ?? 9,
        sortBy: params.sortBy ?? "title",
        ...(params.query ? { query: params.query } : {}),
        ...(params.category ? { category: params.category } : {})
    });
    try {
        return await request(`/books?${search.toString()}`);
    } catch (error) {
        return { items: [], totalItems: 0, page: 0, size: params.size ?? 9 };
    }
}

async function getBookById(bookId) {
    try {
        return await request(`/books/${bookId}`);
    } catch (error) {
        return null;
    }
}

async function addBook(book) {
    try {
        return await request("/books", {
            method: "POST",
            auth: true,
            body: book
        });
    } catch (error) {
        return { success: false, message: error.message };
    }
}

async function updateBook(bookId, book) {
    try {
        return await request(`/books/${bookId}`, {
            method: "PUT",
            auth: true,
            body: book
        });
    } catch (error) {
        return { success: false, message: error.message };
    }
}

async function deleteBook(bookId) {
    try {
        return await request(`/books/${bookId}`, {
            method: "DELETE",
            auth: true
        });
    } catch (error) {
        return { success: false, message: error.message };
    }
}

async function getCart() {
    try {
        return await request("/cart", { auth: true });
    } catch (error) {
        return null;
    }
}

async function addToCart(bookId, quantity = 1) {
    try {
        return await request("/cart/add", {
            method: "POST",
            auth: true,
            body: { bookId, quantity }
        });
    } catch (error) {
        return { success: false, message: error.message };
    }
}

async function removeFromCart(bookId) {
    try {
        return await request(`/cart/${bookId}`, {
            method: "DELETE",
            auth: true
        });
    } catch (error) {
        return null;
    }
}

async function updateCartQuantity(bookId, quantity) {
    try {
        return await request(`/cart/${bookId}/quantity`, {
            method: "PUT",
            auth: true,
            body: { quantity }
        });
    } catch (error) {
        return null;
    }
}

async function clearCart() {
    try {
        return await request("/cart/clear", {
            method: "DELETE",
            auth: true
        });
    } catch (error) {
        return null;
    }
}

async function placeOrder(items, totalPrice) {
    try {
        return await request("/orders", {
            method: "POST",
            auth: true,
            body: { items, totalPrice, status: "PENDING" }
        });
    } catch (error) {
        return { success: false, message: error.message };
    }
}

async function getUserOrders() {
    try {
        return await request("/orders", { auth: true });
    } catch (error) {
        return [];
    }
}

async function getAllOrders() {
    try {
        return await request("/orders/all", { auth: true });
    } catch (error) {
        return [];
    }
}

async function getOrderById(orderId) {
    try {
        return await request(`/orders/${orderId}`, { auth: true });
    } catch (error) {
        return null;
    }
}

async function updateOrderStatus(orderId, status) {
    try {
        return await request(`/orders/${orderId}/status`, {
            method: "PUT",
            auth: true,
            body: { status }
        });
    } catch (error) {
        return { success: false, message: error.message };
    }
}

async function cancelOrder(orderId) {
    try {
        return await request(`/orders/${orderId}/cancel`, {
            method: "PUT",
            auth: true
        });
    } catch (error) {
        return { success: false, message: error.message };
    }
}

async function getWishlist() {
    try {
        return await request("/wishlist", { auth: true });
    } catch (error) {
        return { items: [] };
    }
}

async function addToWishlist(book) {
    try {
        return await request("/wishlist", {
            method: "POST",
            auth: true,
            body: {
                bookId: book.id,
                title: book.title,
                author: book.author,
                price: book.price,
                imageUrl: book.imageUrl,
                category: book.category
            }
        });
    } catch (error) {
        return { success: false, message: error.message };
    }
}

async function removeFromWishlist(bookId) {
    try {
        return await request(`/wishlist/${bookId}`, {
            method: "DELETE",
            auth: true
        });
    } catch (error) {
        return { success: false, message: error.message };
    }
}

async function getAdminStats() {
    try {
        return await request("/admin/stats", { auth: true });
    } catch (error) {
        return null;
    }
}
