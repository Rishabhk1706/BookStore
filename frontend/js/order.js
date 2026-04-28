function getStatusColor(status) {
    const colors = {
        PENDING: "#f59e0b",
        CONFIRMED: "#3b82f6",
        SHIPPED: "#8b5cf6",
        DELIVERED: "#10b981",
        CANCELLED: "#ef4444"
    };
    return colors[status] || "#334155";
}

function renderStatusTimeline(status) {
    const statuses = ["PENDING", "CONFIRMED", "SHIPPED", "DELIVERED"];
    const activeIndex = statuses.indexOf(status);
    if (status === "CANCELLED") {
        return `<div class="status-cancelled">Order cancelled</div>`;
    }

    return `
        <div class="timeline">
            ${statuses.map((step, index) => `
                <div class="timeline-step ${index <= activeIndex ? "active" : ""}">
                    <span>${step}</span>
                </div>
            `).join("")}
        </div>
    `;
}
