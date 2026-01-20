document.addEventListener("DOMContentLoaded", function () {
  const qtyInputs = Array.from(document.querySelectorAll("input.quantity-input"));
  const checkoutBtn = document.getElementById("checkoutBtn");

  let errorBox = document.getElementById("cart-error");
  if (!errorBox) {
    errorBox = document.createElement("div");
    errorBox.id = "cart-error";
    errorBox.style.color = "red";
    errorBox.style.textAlign = "center";
    errorBox.style.fontWeight = "bold";
    // lo metto in alto nel carrello
    const container = document.querySelector(".cart-container") || document.body;
    container.prepend(errorBox);
  }

  function showError(msg) {
    errorBox.textContent = msg || "";
  }

  function isValidQtyValue(v) {
    // solo interi >= 1
    if (v === null || v === undefined) return false;
    if (!/^\d+$/.test(String(v).trim())) return false;
    return parseInt(v, 10) >= 1;
  }

  function refreshState() {
    const hasInvalid = qtyInputs.some((input) => !isValidQtyValue(input.value));

    if (checkoutBtn) checkoutBtn.disabled = hasInvalid;

    if (hasInvalid) {
      showError("Inserisci una quantità valida (numero intero maggiore di 0) prima di procedere al checkout.");
    } else {
      showError("");
    }
  }

  // live validation
  qtyInputs.forEach((input) => {
    input.addEventListener("input", refreshState);
    input.addEventListener("change", refreshState);
  });

  // blocco “extra” sul submit del checkout (se qualcuno forza il click)
  const checkoutForm = checkoutBtn?.closest("form");
  checkoutForm?.addEventListener("submit", function (e) {
    refreshState();
    if (checkoutBtn.disabled) {
      e.preventDefault();
    }
  });

  refreshState();
});
