async function handleLogin(event) {
    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const result = await login(email, password);

    if (result.success) {
        localStorage.setItem("token", result.token);
        localStorage.setItem("userId", result.userId);
        localStorage.setItem("userName", result.name);
        localStorage.setItem("userRole", result.role);
        showToast(`Welcome back, ${result.name}!`, "success");
        window.location.href = "books.html";
        return;
    }

    showToast(result.message || "Login failed", "error");
}

async function handleRegister(event) {
    event.preventDefault();

    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (password !== confirmPassword) {
        showToast("Passwords do not match", "warning");
        return;
    }

    const result = await register(name, email, password);
    if (result.success) {
        showToast("Registration successful. Please login.", "success");
        window.location.href = "login.html";
        return;
    }

    showToast(result.message || "Registration failed", "error");
}

function updateUserInfo() {
    buildNavbar();
}

function checkAuth() {
    if (!isLoggedIn()) {
        window.location.href = "login.html";
    }
}

function checkAdmin() {
    const user = getCurrentUser();
    if (user.role !== "ADMIN") {
        showToast("Admin access required", "warning");
        window.location.href = "books.html";
    }
}
