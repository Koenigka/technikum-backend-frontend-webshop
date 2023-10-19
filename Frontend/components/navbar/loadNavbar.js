$(document).ready(function () {
  // Navbar laden
  $("#navbarContainer").load("/components/navbar/navbar.html", function () {
    // Prüfen, ob ein JWT-Token im sessionStorage vorhanden ist
    if (sessionStorage.getItem("accessToken")) {
      // Benutzer ist eingeloggt

      // Anmelde- und Registrierungslinks ausblenden
      $(".login-link").hide();
      $(".signup-link").hide();

      // Benutzernamen anzeigen
      var username = sessionStorage.getItem("userName");
      $("#username").text(username);
      $(".fa-user").show();

      // Benutzerrollen aus dem Session Storage lesen
      var userRole = JSON.parse(sessionStorage.getItem("userRole"));

      // Überprüfen, ob der Benutzer ein Administrator ist
      if (userRole.includes("ROLE_USER")) {
        // Admin-Link verstecken
        $(".admin-link").hide();
      } else {
        // Admin-Link anzeigen
        $(".fa-user").hide();
      }

      // Warenkorb anzeigen
      $(".cart-link").show();
      $(".fa-basket-shopping").show();
      $("#cartInfo").show();


      var userId = sessionStorage.getItem("userId");
      var accessToken = sessionStorage.getItem("accessToken");

      loadCartContent(userId, accessToken);

      $(".nav-link-basket").on("click", function () {
        // Hier den Code zum Laden des Warenkorb-Inhalts einfügen
        var userId = sessionStorage.getItem("userId");
        var accessToken = sessionStorage.getItem("accessToken");

        loadCartContent(userId, accessToken);

        // Modal öffnen
        $("#customModal").modal("show");
      });

      // Logout hinzufügen
      $("#logout").on("click", function (e) {
        e.preventDefault();
        // Hier die Funktion zum Logout ausführen
        logout();
      });
    } else {
      // Benutzer ist nicht eingeloggt

      // Benutzernamen, Admin-Link und Warenkorb ausblenden
      $("#username").hide();
      $(".fa-user").hide();

      $(".admin-link").hide();
      $(".cart-link").hide();
      $(".fa-basket-shopping").hide();
      $("#cartInfo").hide();
      
    }
  });
});

// Funktion zum Laden des Warenkorb-Inhalts
function loadCartContent(userId, accessToken) {
  var totalPrice = 0;

  $.ajax({
    url: "http://localhost:8080/api/cart/myCart",
    type: "GET",
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
    },
    success: function (data) {
     

      var cartContentHtml = "";

      data.forEach(function (cartItem) {
        var itemPrice = cartItem.price * cartItem.quantity;
        totalPrice += itemPrice;
        cartContentHtml += `


              <div class="row border border-warning rounded shadow-sm mt-2 cart-item" data-id="${
                cartItem.id
              }">
                      <div class="col p-2">              
                      <img src="" alt="${
                        cartItem.title
                      }" class="cart-item-image" />
                      </div>
                      <div class="col p-4 cart-item-info">
                          <p>${cartItem.quantity} x ${cartItem.title}</p>
                          <p>€ ${(cartItem.price * cartItem.quantity).toFixed(
                            2
                          )}</p> 
                          </div>
                      </div>
                  </div>
                  
              `;
      });

      cartContentHtml += `
          <div class="row">
              <div class="col p-2">
                  <p class="fs-3">Subtotal: </p>
              </div>
              <div class="col p-2">
                  <p class="fs-3">€ ${totalPrice.toFixed(2)}</p>
              </div>
          </div>
      `;

      cartContentHtml += `
      <div class="row">
          <div class="col">
            <button class="btn btn-warning text-white shadow"><a href="shop.html" class="text-white text-decoration-none">back to shop</a>
             </button>
          </div>
          <div class="col">
            <button class="btn btn-warning text-white shadow"><a href="cart.html" class="text-white text-decoration-none">show basket</button>
          </div>
        </div>
        `;
      $(".cart-content-modal").html(cartContentHtml);

      fetchAndDisplayCartItemImages(data);

      var totalAmount = totalPrice.toFixed(2);

      $("#cartInfo").text(`€ ${totalAmount}`);
    },
    error: function (error) {
      console.error("Error loading cart content: " + error);
    },
  });
}

function fetchAndDisplayCartItemImages(cartItems) {
  cartItems.forEach(function (cartItem) {
    const apiUrl = "http://localhost:8080/api/files/" + cartItem.img;

    fetch(apiUrl)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.blob();
      })
      .then((blob) => {
        const blobUrl = window.URL.createObjectURL(blob);

        const cartItemElement = $(`.cart-item[data-id="${cartItem.id}"]`);
        const imgElement = cartItemElement.find(".cart-item-image");

        imgElement.attr("src", blobUrl);
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  });
}

// Logout-Funktion
function logout() {
  // Token aus dem Session Storage löschen
  sessionStorage.removeItem("accessToken");

  // Weiterleitung zur Login-Seite oder einer anderen gewünschten Seite
  window.location.href = "../index.html";
}
