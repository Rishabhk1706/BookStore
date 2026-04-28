function calculateCartSummary(cart) {
    const subtotal = (cart?.items || []).reduce((sum, item) => sum + item.total, 0);
    const tax = Number((subtotal * 0.05).toFixed(2));
    const total = subtotal + tax;
    return { subtotal, tax, total };
}
