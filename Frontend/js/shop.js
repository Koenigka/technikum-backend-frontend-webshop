import config from "./config.js";

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
    url: config.baseUrl + config.category.findByActive,
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
    $.ajax({
      url: config.baseUrl + config.product.findByActive,
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
    const apiUrl = `${config.baseUrl}${config.product.findByCategory(
      value,
      true
    )}`;

    $.ajax({
      url: apiUrl,
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
  }

  function createProduct(product) {
    const title = $(
      `<a href="productdetail.html?product=${product.id}" class="text-decoration-none text-warning"><h5 class="card-title text-warning">${product.title}</h5></a>`
    );
    const description = $(` <p class="card-text">${product.description}</p>`);
    const button = $(
      `<button class="btn btn-warning mt-auto text-white">Add to Basket</button>`
    );

    const wrapper = $(`<div class="col-12 col-md-6 col-lg-3 mb-4 ">`);
    const card = $(`<div class="card h-100">`);
    wrapper.append(card);

    const anchor = $(
      `<a href="productdetail.html?product=${product.id}" class="text-decoration-none"></a>`
    );

    const img = $('<img class="card-img-top img-fluid" alt="product-img">');

    card.append(anchor);
    anchor.append(img);
    const baseUrl = config.baseUrl;
    const filesEndpoint = config.file.files;
    const imageReference = product.img;
    const apiUrl = `${baseUrl}${filesEndpoint}/${imageReference}`;

    fetch(apiUrl)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.blob();
      })
      .then((blob) => {
        //console.log('Bild erfolgreich geladen');

        const blobUrl = window.URL.createObjectURL(blob);

        img.attr("src", blobUrl);
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });

    const cardbody = $(`<div class="card-body d-flex flex-column">`);
    card.append(cardbody);
    cardbody.append(title);
    cardbody.append(description);
    cardbody.append(button);
    cardbody.append(`</div>`);
    wrapper.append(`</div>`);

    button.on("click", function () {
      var accessToken = sessionStorage.getItem("accessToken");
      if (!accessToken) {
        // User is not logged in, redirect to login page with a message
        window.location.href =
          "login.html?message=If you are not logged in, you can not proceed Please first log in and then you can continue your action.";
      } else {
        const productId = product.id;
        const quantity = 1;

        const cartItemDto = {
          userId: sessionStorage.getItem("userId"),
          productId: productId,
          quantity: quantity,
        };

        $.ajax({
          url: config.baseUrl + config.cartItem.addToCart,
          type: "POST",
          dataType: "json",
          contentType: "application/json",
          beforeSend: function (xhr) {
            var accessToken = sessionStorage.getItem("accessToken");
            xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
          },
          data: JSON.stringify(cartItemDto),
          success: function (response) {
            
            var userId = sessionStorage.getItem("userId");
            var accessToken = sessionStorage.getItem("accessToken");
            loadCartContent(userId, accessToken);

            if (response.quantity !== undefined) {
              // Update the quantity in your product object
              product.quantity = response.quantity;
            }
            clearToasts();
            showProductAddedToast(product);
            // alert(`"${product.title}" added to basket!`);
          },
          error: function (error) {
            console.error(error);
          },
        });
      }
    });

    return wrapper;
  }

  //Clickfunction Button by value (id)
  $(document).on("click", ".getProductsById", function (event) {
    console.log("value clicked");
    var id = $(this).attr("value");

    const apiUrl = `${config.baseUrl}${config.product.findByCategory(
      id,
      true
    )}`;

    $.ajax({
      url: apiUrl,
      type: "GET",
      cors: true,
      success: function (products) {
        addProducts(products);
        // Check if there are no products for this category
        if (products.length === 0) {
          $(".footer").addClass("fixed-bottom");
        }
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
      url: config.baseUrl + config.product.findByActive,
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
});
