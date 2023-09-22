$(document).ready(function () {

      
             // Hier speichern wir die Produkte und ihre Mengen im Warenkorb
    const cart = {};
     //Gesamtsumme
     var TotalSum = 0;

    // Klicken Sie auf den "In den Warenkorb" Button
    $(document).on("click", "#addToCart", function () {
        // Die Produkt-ID aus dem data-product-id Attribut des Buttons abrufen
        var productId = $(this).data("product-id");
        
        const params = new Proxy(new URLSearchParams(window.location.search), {
          get: (searchParams, prop) => searchParams.get(prop),
         });
        // Get the value of "some_key" in eg "https://example.com/?some_key=some_value"
          let value = params.product; // "some_value"
          console.log(value);

          
      $.ajax({
      url: "http://localhost:8080/api/products/" + productId,
      type: "GET",
      cors: true,
      success: function (product) {
        updateCart(product);
        // Überprüfen, ob der Warenkorb leer ist
        if (cart !== null) {
        // Wenn der Warenkorb leer ist, den leeren-Warenkorb-Text ausblenden
          $("#empty-cart-message").hide();
        }
      },
      error: function (error) {
        console.error(error);
      },
    });

        // Prüfen, ob das Produkt bereits im Warenkorb ist
        if (cart[productId] === undefined) {
            // Wenn das Produkt noch nicht im Warenkorb ist, setze die Menge auf 1
            cart[productId] = 1;
        } else {
            // Wenn das Produkt bereits im Warenkorb ist, erhöhe die Menge um 1
            cart[productId]++;
        }

        // Aktualisiere den Warenkorb im HTML
        updateCart();

    });

    // Funktion zum Aktualisieren des Warenkorbs im HTML
    function updateCart(product) {
      var cartItems = $("#cart-items");
  
      // Die Menge für das aktuelle Produkt
      var productId = product.id; // Annahme, dass das Produkt eine id hat
      var quantity = cart[productId];
  
      // Hier können Sie den Produktnamen und Preis aus einer Datenquelle abrufen
      var productName = product.title; // Beispiel: Hier den richtigen Produktnamen einfügen
      var productPrice = product.price; // Beispiel: Hier den richtigen Preis einfügen
  
     
      TotalSum += product.price;
      $("#cart-total-amount").text(TotalSum.toFixed(2));

      // Überprüfen, ob das Produkt bereits in der Liste ist
      var existingItem = cartItems.find("#cart-item-" + productId);
  
      if (existingItem.length > 0) {
          // Wenn das Produkt bereits in der Liste ist, aktualisiere die Menge
          existingItem.find(".cart-item-quantity").text(quantity);
          existingItem.find(".cart-item-price").text("$" + (productPrice * quantity));
        } else {
          // Wenn das Produkt nicht in der Liste ist, füge es hinzu
          var itemHTML = "<li id='cart-item-" + productId + "'>" +
              productName + " x <span class='cart-item-quantity'>" + quantity + "</span> - <span class='cart-item-price'>$" + (productPrice * quantity) + "</span>" +
              "</li>";
          cartItems.append(itemHTML);
        }

        // AJAX-Aufruf zum Hinzufügen des Produkts zum Warenkorb
      $.ajax({
        url: "http://localhost:8080/api/carts", // Ihr Backend-Endpunkt zum Hinzufügen von Produkten zum Warenkorb
        type: "POST", // Verwenden Sie POST, um Daten an den Server zu senden
        contentType: "application/json",
        data: JSON.stringify({ userId: userId, productId: productId, quantity: quantity }),
        success: function (response) {
          console.log("Product added to cart successfully");
          // Hier können Sie eine Bestätigungsnachricht anzeigen oder andere Aktionen ausführen
        },
        error: function (error) {
          console.error(error);
          // Hier können Sie eine Fehlermeldung anzeigen oder andere Fehlerbehandlung durchführen
        },
  }); 
        
  }
});
