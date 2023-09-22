$(document).ready(function () {
  const params = new Proxy(new URLSearchParams(window.location.search), {
    get: (searchParams, prop) => searchParams.get(prop),
  });

  
  // Get the value of "some_key" in eg "https://example.com/?some_key=some_value"
  let value = params.category; // "some_value"
  //console.log(value);
  var loginMessage = sessionStorage.getItem("loginMessage");

  // Wenn eine Erfolgsmeldung vorhanden ist, anzeigen und aus dem Web Storage entfernen
  if (loginMessage) {
    // Erfolgsmeldung anzeigen
    $("#loginSuccessMessage").text(loginMessage);
    $("#loginSuccessMessage").show();

    // Erfolgsmeldung nach einigen Sekunden ausblenden
    setTimeout(function () {
      $("#loginSuccessMessage").hide();
    }, 7000);

    // Erfolgsmeldung aus dem Web Storage entfernen
    sessionStorage.removeItem("loginMessage");
  }

  //Buttons mit Kategorien laden
  $.ajax({
    url: "http://localhost:8080/api/categories/isActive/" + true,
    type: "GET",
    cors: true,
    success: function (categories) {
      addCategories(categories);
    },
    error: function (error) {
      console.error(error);
    },
  });

  function addCategories(categories) {
    const allCategories = $("#categories");
    allCategories.empty();
    const all = $(
      `<li ><button class="dropdown-item text-center text-white fs-3" type="button" id="allCategories">All Categories</button></li> `
    );
    allCategories.append(all);
    for (let category of categories) {
      allCategories.append(createCategory(category));
    }
  }

  function createCategory(category) {
    const button = $(
      `<li><button class="dropdown-item getProductsById text-center text-white fs-3" type="button" value="${category.id}">${category.title}</button></li>`
    );
    return button;
  }

  if (value == null) {
    //Alle Produkte laden
    $.ajax({
      url: "http://localhost:8080/api/products/isActive/" + true,
      type: "GET",
      cors: true,
      success: function (products) {
        addProducts(products);
      },
      error: function (error) {
        console.error(error);
      },
    });
  }

  if (value != null) {
    $.ajax({
      url: "http://localhost:8080/api/products/byCategory/" + value + "/" + true,
      type: "GET",
      cors: true,
      success: function (products) {
        addProducts(products);
      },
      error: function (error) {
        console.error(error);
      },
    });
  }

  function addProducts(products) {
    const allProducts = $("#products");
    allProducts.empty();
    for (let product of products) {
      allProducts.append(createProduct(product));
    }

  // Bei Klick auf + oder - prüfen und Wert anpassen
  function updateQuantity(element, increment) {
    const input = $(element).siblings(".form-control");
    let quantity = parseInt(input.val());
    if (isNaN(quantity) || quantity <= 0) {
      quantity = 1; // Setze auf 1 zurück, wenn ungültiger Wert
    }
    input.val(quantity);
}

// Click-Handler für + und -
$(document).on("click", ".btn-outline-secondary", function () {
  const increment = $(this).index() === 1;
  updateQuantity(this, increment);
});

  function createProduct(product) {
    const img = $(`<a href="productdetail.html?product=${product.id}"><img src="../${product.img}" class="card-img-top img-fluid" alt="..."></a>`);
  
    const title = $(`<a href="productdetail.html?product=${product.id}" class="text-decoration-none text-warning"><h5 class="card-title text-warning">${product.title}</h5></a>`);
    const description = $(`<p class="card-text">${product.description}</p>`);
    const quantityInput = $(`
      <div class="input-group mb-3">
        <button class="btn btn-outline-secondary" type="button" onclick="decrementQuantity()">-</button>
        <input type="number" class="form-control" id="quantity" min="1" value="1">
        <button class="btn btn-outline-secondary" type="button" onclick="incrementQuantity()">+</button>
      </div>
    `);
    const button = $(`<button class="btn btn-warning mt-auto text-white" id="addToCart" data-product-id="${product.id}">Add to Basket</button>`);
  
    const wrapper = $(`<div class="col-12 col-md-6 col-lg-3 mb-4">`);
    const card = $(`<div class="card h-100">`);
    wrapper.append(card);
    card.append(img);
    const cardbody = $(`<div class="card-body d-flex flex-column">`);
    card.append(cardbody);
    cardbody.append(title);
    cardbody.append(description);
    const quantityWrapper = $(`<div class="quantity-wrapper">`);
    quantityWrapper.append(quantityInput);
    cardbody.append(quantityWrapper);
    cardbody.append(button);
    wrapper.append(`</div>`);
  
    return wrapper;
  }  

  //Clickfunction Button by value (id)
  $(document).on("click", ".getProductsById", function (event) {
    console.log("value clicked");
    id = $(this).attr("value");
    $.ajax({
      url: "http://localhost:8080/api/products/byCategory/" + id + "/" + true,
      type: "GET",
      cors: true,
      success: function (products) {
        addProducts(products);
      },
      error: function (error) {
        console.error(error);
      },
    });
  });

  //Clickfunction allCategories

  $(document).on("click", "#allCategories", function (event) {
    console.log("allCategories clicked");
    $.ajax({
      url: "http://localhost:8080/api/products/isActive/" + true,
      type: "GET",
      cors: true,
      success: function (products) {
        addProducts(products);
      },
      error: function (error) {
        console.error(error);
      },
    });
  });

    //KATHI

  //Stückzahl 
  function incrementQuantity() {
    const input = $(this).siblings('.form-control');
    let quantity = parseInt(input.val());
    quantity++;
    input.val(quantity);
  }

  function decrementQuantity() {
    const input = $(this).siblings('.form-control');
    let quantity = parseInt(input.val());
    if (quantity > 1) {
      quantity--;
      input.val(quantity);
    }
  }

  $(document).on('click', '.btn-outline-secondary:first-child', decrementQuantity);
  $(document).on('click', '.btn-outline-secondary:last-child', incrementQuantity);


    // Handler für den Klick auf "Add to cart"
$(document).on("click", "#addToCart", function(event) {
  // Hier die Produkt-ID extrahieren (z. B. aus einem Datenattribut)
  var productId = $(this).data("product-id");

  // Die ausgewählte Menge aus dem zugehörigen Eingabefeld lesen
  var quantity = parseInt($(this).siblings(".quantity-wrapper").find(".form-control").val());

  // Funktionalität zum Hinzufügen des Produkts zum Warenkorb hier implementieren
  // (z. B. durch einen AJAX-Aufruf an den Server, um das Produkt in den Warenkorb des Benutzers zu legen)

  // Füge das Produkt und die Menge zum Warenkorb hinzu (vorübergehend in der Frontend-Speicherung)
  addToBasket(productId, quantity);

  // Beispiel: Produkt-ID und Menge in der Konsole ausgeben (dieser Teil muss durch den eigentlichen Warenkorb-Code ersetzt werden)
  console.log("Produkt-ID hinzufügen zum Warenkorb:", productId, "Menge:", quantity);
});
  
  //------------


  }});
