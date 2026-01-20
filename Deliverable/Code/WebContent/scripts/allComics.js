document.addEventListener('DOMContentLoaded', () => {
    const forms = document.querySelectorAll('.add-to-cart-form');

    forms.forEach(form => {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();

            const comicId = form.dataset.comicId;

            try {
                const response = await fetch('cart', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: new URLSearchParams({ comicId })
                });

                const data = await response.json();

                if (data.success) {
                   
                    const cartCount = document.querySelector('.cart-count');
                    if (cartCount) {
                        cartCount.textContent = data.cartCount;
                        cartCount.style.display = 'inline';
                    }

                    const message = document.getElementById('cart-message');
                    if (message) {
                        message.textContent = 'Fumetto aggiunto al carrello!';
                        message.style.display = 'block';

                        setTimeout(() => {
                            message.style.display = 'none';
                        }, 3000);
                    }
                } else {
                    alert('Errore: ' + data.message);
                }
            } catch (err) {
                alert('Errore di comunicazione con il server.');
            }
        });
    });
});
