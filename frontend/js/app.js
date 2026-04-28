const APP_STORAGE_KEYS = {
    tutorialSeen: "bookstoreTutorialSeen",
    orderConfirmation: "bookstoreOrderConfirmation"
};

function getCurrentPage() {
    const fileName = window.location.pathname.split("/").pop();
    return fileName || "index.html";
}

function isTokenExpired(token) {
    if (!token) {
        return true;
    }

    try {
        const payload = JSON.parse(atob(token.split(".")[1]));
        return payload.exp ? payload.exp * 1000 < Date.now() : false;
    } catch (error) {
        return true;
    }
}

function clearSession() {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    localStorage.removeItem("userRole");
}

function ensureValidSession() {
    const token = localStorage.getItem("token");
    if (token && isTokenExpired(token)) {
        clearSession();
        showToast("Your session expired. Please login again.", "warning");
        if (getCurrentPage() !== "login.html") {
            window.location.href = "login.html";
        }
    }
}

function buildNavbar() {
    const user = getCurrentUser();
    const isAdmin = user.role === "ADMIN";
    const activePage = getCurrentPage();
    const nav = document.getElementById("mainNav");

    if (!nav) {
        return;
    }

    nav.innerHTML = `
        <a class="brand" href="index.html">
            <span class="brand-mark">📚</span>
            <span>BookStore Pro</span>
        </a>
        <button class="nav-toggle" id="navToggle" aria-label="Toggle navigation">☰</button>
        <div class="nav-shell" id="navShell">
            <div class="nav-links">
                ${navLink("index.html", "🏠", "Home", activePage)}
                ${navLink("books.html", "📘", "Books", activePage)}
                ${navLink("wishlist.html", "💖", "Wishlist", activePage)}
                ${navLink("cart.html", "🛒", "Cart", activePage)}
                ${navLink("orders.html", "📦", "Orders", activePage)}
                ${isAdmin ? navLink("admin.html", "🛠️", "Admin", activePage) : ""}
                ${navLink("profile.html", "👤", "Profile", activePage)}
            </div>
            <div class="nav-user" id="navUser"></div>
        </div>
    `;

    const navUser = document.getElementById("navUser");
    if (isLoggedIn()) {
        navUser.innerHTML = `
            <div class="welcome-chip">
                <span class="welcome-eyebrow">Welcome back</span>
                <strong>${user.name || "Reader"}</strong>
            </div>
            <button class="button ghost" onclick="logout()">Logout</button>
        `;
    } else {
        navUser.innerHTML = `
            <a class="button ghost" href="login.html">Login</a>
            <a class="button" href="signup.html">Sign Up</a>
        `;
    }

    const toggle = document.getElementById("navToggle");
    const navShell = document.getElementById("navShell");
    if (toggle && navShell) {
        toggle.addEventListener("click", () => {
            navShell.classList.toggle("open");
        });
    }
}

function navLink(href, icon, label, activePage) {
    const isActive = activePage === href ? "active" : "";
    return `<a class="nav-link ${isActive}" href="${href}"><span>${icon}</span><span>${label}</span></a>`;
}

function injectShell() {
    if (!document.getElementById("mainNav")) {
        return;
    }
    buildNavbar();
    if (!document.getElementById("toastContainer")) {
        const toastContainer = document.createElement("div");
        toastContainer.id = "toastContainer";
        toastContainer.className = "toast-container";
        document.body.appendChild(toastContainer);
    }
}

function showToast(message, type = "info") {
    const toastContainer = document.getElementById("toastContainer");
    if (!toastContainer) {
        alert(message);
        return;
    }

    const toast = document.createElement("div");
    toast.className = `toast ${type}`;
    toast.textContent = message;
    toastContainer.appendChild(toast);

    requestAnimationFrame(() => toast.classList.add("show"));
    setTimeout(() => {
        toast.classList.remove("show");
        setTimeout(() => toast.remove(), 250);
    }, 3000);
}

function setLoading(targetId, message = "Loading...") {
    const target = document.getElementById(targetId);
    if (!target) {
        return;
    }

    target.innerHTML = `
        <div class="loading-state">
            <div class="spinner"></div>
            <p>${message}</p>
        </div>
    `;
}

function renderPageHint(targetId, title, body) {
    const target = document.getElementById(targetId);
    if (!target) {
        return;
    }

    target.innerHTML = `
        <div class="hint-card">
            <h3>${title}</h3>
            <p>${body}</p>
        </div>
    `;
}

function showTutorialIfNeeded() {
    if (getCurrentPage() !== "index.html" || localStorage.getItem(APP_STORAGE_KEYS.tutorialSeen)) {
        return;
    }

    const modal = document.createElement("div");
    modal.className = "modal-backdrop";
    modal.innerHTML = `
        <div class="modal-card">
            <h2>Welcome to BookStore Pro</h2>
            <p>This quick guide will help first-time users understand how to use the store.</p>
            <div class="tutorial-steps">
                <div><strong>1. Create account:</strong> Sign up or login from the top right.</div>
                <div><strong>2. Browse books:</strong> Search, filter, sort, and open book details.</div>
                <div><strong>3. Save or buy:</strong> Add books to wishlist or cart.</div>
                <div><strong>4. Checkout:</strong> Review pricing and place your order.</div>
                <div><strong>5. Track progress:</strong> Visit Orders to follow status updates.</div>
            </div>
            <div class="modal-actions">
                <button class="button" id="closeTutorial">Got it</button>
            </div>
        </div>
    `;
    document.body.appendChild(modal);

    document.getElementById("closeTutorial").addEventListener("click", () => {
        localStorage.setItem(APP_STORAGE_KEYS.tutorialSeen, "true");
        modal.remove();
    });
}

function saveOrderConfirmation(order) {
    sessionStorage.setItem(APP_STORAGE_KEYS.orderConfirmation, JSON.stringify(order));
}

function getOrderConfirmation() {
    const value = sessionStorage.getItem(APP_STORAGE_KEYS.orderConfirmation);
    return value ? JSON.parse(value) : null;
}

function formatCurrency(amount) {
    return `₹${Number(amount || 0).toFixed(2)}`;
}

document.addEventListener("DOMContentLoaded", () => {
    ensureValidSession();
    injectShell();
    showTutorialIfNeeded();
});
