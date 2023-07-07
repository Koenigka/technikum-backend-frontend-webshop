$(document).ready(function () {
  //USER REGISTRATION
  $("#registerUserButton").on("click", (_e) => {
    const user = {
      title: $("#title").val(),
      firstname: $("#first-name").val(),
      lastname: $("#last-name").val(),
      address: $("#inputAddress").val(),
      city: $("#inputCity").val(),
      zip: $("#inputZip").val(),
      username: $("#un").val(),
      email: $("#email").val(),
      password: $("#password1").val(),
      active: "true",
      admin: "false",
    };
    $.ajax({
      url: "http://localhost:8080/users",
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(user),
      success: function (response) {
        // Erfolgsmeldung im Web Storage speichern
        sessionStorage.setItem(
          "registrationMessage",
          "Registrierung erfolgreich. Bitte loggen Sie sich ein."
        );
        // Weiterleitung zur Login-Seite
        window.location.href = "login.html";
      },
      error: function (xhr, status, error) {
        // Fehlermeldung anzeigen
        $("#registrationMessage").text(
          "Fehler bei der Registrierung: " + error
        );
      },
    });
  });
});
