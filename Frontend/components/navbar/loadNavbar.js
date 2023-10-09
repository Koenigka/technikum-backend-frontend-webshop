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
    }
  });
});

// Logout-Funktion
function logout() {
  // Token aus dem Session Storage löschen
  sessionStorage.removeItem("accessToken");

  // Weiterleitung zur Login-Seite oder einer anderen gewünschten Seite
  window.location.href = "../index.html";
}
