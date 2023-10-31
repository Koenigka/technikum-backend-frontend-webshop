import config from "./config.js";

$(document).ready(function () {
  let product;

  const params = new Proxy(new URLSearchParams(window.location.search), {
    get: (searchParams, prop) => searchParams.get(prop),
  });
  // Get the value of "some_key" in eg "https://example.com/?some_key=some_value"
  let value = params.product; // "some_value"

  $.ajax({
    url: config.baseUrl + config.product.findById + value,
    type: "GET",
    cors: true,
    success: function (result) {
      product = result;
      addProduct(product);
    },
    error: function (error) {
      console.error(error);
    },
  });

  function addProduct(product) {
    const productd = $("#product");
    productd.empty();

    const productdetail = $(`
        <div class="row pt-3">
        <div class="col-lg-6 col-md-6 col-sm-12">
          <img class="inner-img img-fluid rounded border border-warning"  width="350px"
            height="250px" />
        </div>
        <div class="col-lg-6 col-md-6 col-sm-12">
          <h3 class="text-warning text-decoration-none fs-1">
            <strong>${product.title}</strong>
          </h3>
          <p class="bg-white border border-warning rounded px-5 py-3 fs-5">
          <b>${product.description}</b>
          </p>
          <p class="bg-white border border-warning rounded px-3 py-2 fs-5 mx-5">
            <em>
                inkl. 7 % MwSt. zzgl. Versandkosten <br />
                
                100% Handmade vom Bäckermeister</em>
          </p>
          <p class="fs-3 text-warning"><b>EUR ${product.price}</b></p>
          <div class="qty mt-5 mb-3 counter">
            <span class="minus bg-light fs-3">-</span>
            <input type="number" class="count" name="qty" id="quantityInput" value="1" />
            <span class="plus bg-light fs-3">+</span>
          </div>
          <button id="addToBasketButton" class="btn btn-warning mt-auto mb-5 text-white">Add to Basket</button>
          </div>
      </div>`);

    productd.append(productdetail);

    const img = productdetail.find("img");
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
        const blobUrl = window.URL.createObjectURL(blob);
        img.attr("src", blobUrl);
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  }

  $(document).on("click", "#addToBasketButton", function (event) {

    var accessToken = sessionStorage.getItem("accessToken");
    if (!accessToken) {
      // User is not logged in, redirect to login page with a message
      window.location.href =
        "login.html?message=If you are not logged in, you can not proceed Please first log in and then you can continue your action.";
    } else {
      const productId = product.id;
      const quantity = parseInt(document.getElementById("quantityInput").value);

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
          showProductAddedToast(product, event);
          // alert(`"${product.title}" added to basket!`);
        },
        error: function (error) {
          console.error(error);
        },
      });
    }
  });
});
