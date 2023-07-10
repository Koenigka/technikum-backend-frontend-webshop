$(document).ready(function () {
  // Erfolgsmeldung aus dem Web Storage abrufen
  var registrationMessage = sessionStorage.getItem("registrationMessage");

  // Wenn eine Erfolgsmeldung vorhanden ist, anzeigen und aus dem Web Storage entfernen
  if (registrationMessage) {
    // Erfolgsmeldung anzeigen
    $("#registrationSuccessMessage").text(registrationMessage);
    $("#registrationSuccessMessage").show();

    // Erfolgsmeldung nach einigen Sekunden ausblenden
    setTimeout(function () {
      $("#registrationSuccessMessage").hide();
    }, 15000);
  }
  // Erfolgsmeldung aus dem Web Storage entfernen
  sessionStorage.removeItem("registrationMessage");
  // LOGIN
  $("#loginUserButton").on("click", function () {
    var email = $("#email").val();
    var password = $("#password").val();

    var loginData = {
      email: email,
      password: password,
    };

    $.ajax({
      url: "http://localhost:8080/api/auth/login",
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(loginData),
      success: function (response) {
        // Erfolgsmeldung anzeigen und weitere Aktionen durchführen
        console.log("Login erfolgreich");
        console.log(response.accessToken);

        // JWT-Token im sessionStorage speichern
        sessionStorage.setItem("accessToken", response.accessToken);

        // Token decodieren und Benutzerrolle extrahieren
        var token = response.accessToken;
        var decodedToken = jwt_decode(token);

        console.log(decodedToken);

        var userName = decodedToken.e;
        sessionStorage.setItem("userName", userName);

        var userRole = decodedToken.a; // Das gesamte Array der Benutzerrollen

        // Benutzerrolle im Session Storage speichern
        sessionStorage.setItem("userRole", JSON.stringify(userRole));
        console.log("UserRoleLogIn:", userRole);

        //Zugriff auf den Token über: var accessToken = sessionStorage.getItem("accessToken");

        // Weiterleitung nach dem erfolgreichen Login
        window.location.href = "/pages/shop.html";
      },
      error: function (xhr, status, error) {
        // Fehlermeldung anzeigen
        console.error("Fehler beim Login: " + error);
      },
    });
  });
});
