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
      url:
        "http://localhost:8080/api/products/byCategory/" + value + "/" + true,
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
    const img = $(
      `<a  href="productdetail.html?product=${product.id}"><img src="../${product.img}" class="card-img-top img-fluid" alt="..."></a>`
    );

    const title = $(
      `<a href="productdetail.html?product=${product.id}" class="text-decoration-none text-warning"><h5 class="card-title text-warning">${product.title}</h5></a>`
    );
    const description = $(` <p class="card-text">${product.description}</p>`);
    const button = $(
      `<button class="btn btn-warning mt-auto text-white">Add to Basket</button>`
    );
    // Create a custom message element
    const message = $(`
  <div class="alert alert-success alert-dismissible fade show" role="alert">
    "${product.title}" added to cart!
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
  </div>
`);

    const wrapper = $(`<div class="col-12 col-md-6 col-lg-3 mb-4 ">`);
    const card = $(`<div class="card h-100">`);
    wrapper.append(card);
    card.append(img);
    const cardbody = $(`<div class="card-body d-flex flex-column">`);
    card.append(cardbody);
    cardbody.append(title);
    cardbody.append(description);
    cardbody.append(button);
    cardbody.append(`</div>`);
    wrapper.append(`</div>`);
    wrapper.append(message);

    // Add a click event to the button that checks if the user is logged in before redirecting
    button.on("click", function () {
      var accessToken = sessionStorage.getItem("accessToken");
      if (!accessToken) {
        // User is not logged in, redirect to login page with a message
        window.location.href =
          "login.html?message=If you are not logged in, you can not proceed Please first log in and then you can continue your action.";
      } else {
        //***** Implement the logic to add the product to the cart here ********
        alert(`"${product.title}" added to cart!`);

        // Automatically close the alert after 5 seconds (5000 milliseconds)
        setTimeout(() => {
          // Close the alert by setting its text to an empty string
          alert("");
        }, 5000);
      }
    });

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
});
