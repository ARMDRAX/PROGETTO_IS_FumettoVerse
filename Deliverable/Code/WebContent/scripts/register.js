document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("registerForm");
  if (!form) return;

  const nameInput = document.getElementById("name");
  const emailInput = document.getElementById("email");
  const passwordInput = document.getElementById("password");
  const securityQuestionSelect = document.getElementById("securityQuestion");
  const securityAnswerInput = document.getElementById("securityAnswer");
  const errorContainer = document.getElementById("errorMessages");

  function isValidEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  function isValidPassword(password) {
    const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
    return regex.test(password);
  }

  function hasAtLeastOneLetter(text) {
    return /[A-Za-zÀ-ÖØ-öø-ÿ]/.test(text);
  }

  function showErrors(errors) {
    if (!errorContainer) return;
    errorContainer.innerHTML = "";
    errors.forEach((err) => {
      const p = document.createElement("p");
      p.textContent = err;
      p.style.color = "red";
      p.style.textAlign = "center";
      p.style.fontWeight = "bold";
      errorContainer.appendChild(p);
    });
  }

  function validateAll() {
    const errors = [];

    if (!nameInput.value || nameInput.value.trim() === "") {
      errors.push("Il nome è obbligatorio.");
    }

    if (!isValidEmail(emailInput.value || "")) {
      errors.push("Inserisci un indirizzo email valido.");
    }

    if (!isValidPassword(passwordInput.value || "")) {
      errors.push("La password deve essere lunga almeno 8 caratteri e contenere almeno una lettera e un numero.");
    }

    if (!securityQuestionSelect || securityQuestionSelect.value === "") {
      errors.push("Seleziona una domanda di sicurezza.");
    }

    const secAns = (securityAnswerInput?.value || "").trim();
    if (!secAns) {
      errors.push("Inserisci la risposta di sicurezza.");
    } else if (!hasAtLeastOneLetter(secAns)) {
      errors.push("La risposta di sicurezza deve contenere almeno una lettera.");
    }

    return errors;
  }

  // niente ENTER submit accidentale
  form.addEventListener("keydown", function (e) {
    if (e.key === "Enter" && e.target && e.target.tagName === "INPUT") {
      e.preventDefault();
    }
  });

  form.addEventListener("submit", function (event) {
    const errors = validateAll();
    if (errors.length > 0) {
      event.preventDefault();
      showErrors(errors);
    }
  });
});
