$(document).ready(function () {
  if (sessionStorage.getItem("accessToken")) {
    // Benutzer ist eingeloggt

    // Anmelde- und Registrierungslinks ausblenden
    $(".login-link").hide();
    $(".signup-link").hide();

    // Benutzernamen anzeigen
    var username = "UserName"; // Hier den Benutzernamen aus dem Token oder der Serverantwort einfügen
    $("#username").text(username);

    // Überprüfen, ob der Benutzer ein Administrator ist
    var isAdmin = true; // Hier die Überprüfung, ob der Benutzer ein Administrator ist, einfügen

    if (isAdmin) {
      // Admin-Link anzeigen
      $(".admin-link").show();
    }
  } else {
    // Benutzer ist nicht eingeloggt

    // Benutzernamen und Admin-Link ausblenden
    $("#username").hide();
    $(".admin-link").hide();
  }

  $.ajax({
    url: "http://localhost:8080/categories/isActive/" + true,
    type: "GET",
    cors: true,
    auth:
      window.sessionStorage.getItem("token") !== null
        ? window.sessionStorage.getItem("token")
        : "",
    success: function (categories) {
      addCategoriesToPage(categories);
    },
    error: function (error) {
      console.error(error);
    },
  });

  function addCategoriesToPage(categories) {
    const categoriesContainer = $("#categoriesContainer");
    categoriesContainer.empty();

    for (let category of categories) {
      categoriesContainer.append(createCategory(category));
    }
  }

  function createCategory(category) {
    const img = $(
      `<a href="pages/shop.html?category=${category.id}"><img src="${category.imgUrl}" class="card-img-top" alt="..."></a>`
    );
    const title = $(`<h5 class="card-title">${category.title}</h5>`);
    const description = $(`<p class="card-text">${category.description}</p>`);
    const button = $(
      `<a href="shop.html?category=${category.id}" class="btn btn-warning text-white mt-auto">Buy ${category.title}</a>`
    );

    const wrapper = $(`<div class="col-12 col-md-6 col-lg-3 mb-4 ">`);
    const card = $(`<div class="card h-100" style="width: 100%;">`);
    wrapper.append(card);
    card.append(img);
    const cardbody = $(`<div class="card-body d-flex flex-column">`);
    card.append(cardbody);
    cardbody.append(title);
    cardbody.append(description);
    cardbody.append(button);
    cardbody.append(`</div>`);
    wrapper.append(`</div>`);

    return wrapper;
  }
});
