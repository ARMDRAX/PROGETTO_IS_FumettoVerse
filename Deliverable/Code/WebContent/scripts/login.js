document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("loginForm");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const errorContainer = document.getElementById("errorMessages");

    function isValidEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }

    function showErrors(errors) {
        errorContainer.innerHTML = "";
        errors.forEach(err => {
            const p = document.createElement("p");
            p.textContent = err;
            p.style.color = "red";
            p.style.textAlign = "center";
            p.style.fontWeight = "bold";
            errorContainer.appendChild(p);
        });
    }

    form.addEventListener("submit", function(event) {
        const errors = [];

        if (!isValidEmail(emailInput.value)) {
            errors.push("Inserisci un indirizzo email valido.");
        }
        if (passwordInput.value.trim() === "") {
            errors.push("Inserisci la password.");
        }

        if (errors.length > 0) {
            event.preventDefault();
            showErrors(errors);
        }
    });

    [emailInput, passwordInput].forEach(input => {
        input.addEventListener("change", function() {
            const errors = [];

            if (input === emailInput && !isValidEmail(emailInput.value)) {
                errors.push("Inserisci un indirizzo email valido.");
            }
            if (input === passwordInput && passwordInput.value.trim() === "") {
                errors.push("Inserisci la password.");
            }

            if (errors.length > 0) {
                showErrors(errors);
            } else {
                errorContainer.innerHTML = "";
            }
        });
    });
});