function isExpiryNotPast(mmYY) {
  const m = mmYY.match(/^(0[1-9]|1[0-2])\/(\d{2})$/);
  if (!m) return false;

  const month = parseInt(m[1], 10);
  const year = 2000 + parseInt(m[2], 10);

  const now = new Date();
  const currentYear = now.getFullYear();
  const currentMonth = now.getMonth() + 1; // 1..12

  return (year > currentYear) || (year === currentYear && month >= currentMonth);
}

function toggleCardDetails() {
  const selected = document.querySelector('input[name="paymentMethod"]:checked');
  const cardDetails = document.getElementById("card-details");

  if (!selected) {
    cardDetails.style.display = "none";
    return;
  }

  const isCard = selected.value === "Carta di Credito";
  cardDetails.style.display = isCard ? "block" : "none";

  const cardHolderEl = document.getElementById("cardHolder");
  const cardNumberEl = document.getElementById("cardNumber");
  const expiryEl = document.getElementById("expiry");
  const cvvEl = document.getElementById("cvv");

  // Required solo se carta
  if (cardHolderEl) cardHolderEl.required = isCard;
  if (cardNumberEl) cardNumberEl.required = isCard;
  if (expiryEl) expiryEl.required = isCard;
  if (cvvEl) cvvEl.required = isCard;

  // Se non è carta, pulisco i campi per evitare submit "sporchi"
  if (!isCard) {
    if (cardHolderEl) cardHolderEl.value = "";
    if (cardNumberEl) cardNumberEl.value = "";
    if (expiryEl) expiryEl.value = "";
    if (cvvEl) cvvEl.value = "";
  }
}

document.addEventListener("DOMContentLoaded", function () {
  const form = document.querySelector("form");
  const errorDiv = document.getElementById("error-message");

  // Nel caso sia già selezionato un metodo al caricamento
  toggleCardDetails();

  form.addEventListener("submit", function (event) {
    errorDiv.innerHTML = "";

    const paymentMethodElem = document.querySelector('input[name="paymentMethod"]:checked');
    if (!paymentMethodElem) {
      errorDiv.innerText = "Seleziona un metodo di pagamento.";
      event.preventDefault();
      return;
    }

    const paymentMethod = paymentMethodElem.value;

    const address = document.getElementById("address").value.trim();
    const houseNumber = document.getElementById("houseNumber").value.trim();
    const zip = document.getElementById("zip").value.trim();
    const city = document.getElementById("city").value.trim();

    const errors = [];

    // VIA: obbligatoria + almeno 3 lettere
    if (!address) {
      errors.push("Inserisci la via.");
    } else {
      const letters = address.match(/[A-Za-zÀ-ÖØ-öø-ÿ]/g);
      if (!letters || letters.length < 3) {
        errors.push("La via deve contenere almeno 3 lettere.");
      }
    }

    // CIVICO: obbligatorio + formato
    if (!houseNumber) {
      errors.push("Inserisci il numero civico.");
    } else if (!/^[0-9]{1,4}[A-Za-z]?$/.test(houseNumber)) {
      errors.push("Numero civico non valido (es. 12 o 12A).");
    }

    // CAP
    if (!/^\d{5}$/.test(zip)) {
      errors.push("Inserisci un CAP valido (5 cifre).");
    }

    // CITTA': obbligatoria + almeno 2 lettere
    if (!city) {
      errors.push("Inserisci la città.");
    } else {
      const lettersCity = city.match(/[A-Za-zÀ-ÖØ-öø-ÿ]/g);
      if (!lettersCity || lettersCity.length < 2) {
        errors.push("La città deve contenere almeno 2 lettere.");
      }
    }

    // CARTA DI CREDITO (solo se selezionata)
    if (paymentMethod === "Carta di Credito") {
      const cardHolder = document.getElementById("cardHolder").value.trim();
      const cardNumber = document.getElementById("cardNumber").value.trim();
      const expiry = document.getElementById("expiry").value.trim();
      const cvv = document.getElementById("cvv").value.trim();

      // Intestatario obbligatorio + formato
      if (!cardHolder) {
        errors.push("Inserisci il nome intestatario.");
      } else if (!/^[A-Za-zÀ-ÖØ-öø-ÿ' ]{2,40}$/.test(cardHolder)) {
        errors.push("Nome intestatario non valido.");
      }

      // Numero carta
      if (!cardNumber) {
        errors.push("Inserisci il numero carta.");
      } else if (!/^[0-9]{13,19}$/.test(cardNumber)) {
        errors.push("Numero carta non valido.");
      }

      // Scadenza: formato + non nel passato
      if (!expiry) {
        errors.push("Inserisci la scadenza della carta.");
      } else if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(expiry)) {
        errors.push("Scadenza carta non valida (MM/AA).");
      } else if (!isExpiryNotPast(expiry)) {
        errors.push("La carta risulta scaduta.");
      }

      // CVV
      if (!cvv) {
        errors.push("Inserisci il CVV.");
      } else if (!/^\d{3,4}$/.test(cvv)) {
        errors.push("CVV non valido.");
      }
    }

    if (errors.length > 0) {
      errorDiv.innerHTML = errors.map(e => `<div>${e}</div>`).join("");
      event.preventDefault();
    }
  });
});

