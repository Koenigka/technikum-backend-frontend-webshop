import config from "./config.js";

$(document).ready(function () {
  const userId = sessionStorage.getItem("userId");
  const accessToken = sessionStorage.getItem("accessToken");

  // Function to check and update footer class based on cart items count
  function updateFooterClass() {
    const cartItems = $(".cart-item");
    if (cartItems.length <= 1) {
      $(".footer").addClass("fixed-bottom");
    } else {
      $(".footer").removeClass("fixed-bottom");
    }
  }
  fetchAndDisplayCartItems();
  function fetchAndDisplayCartItems() {
    var totalPrice = 0;
    const productQuantities = {}; // To keep track of product quantities
    // Make an AJAX request to fetch cart items
    $.ajax({
      url: config.baseUrl + config.cartItem.myCart,
      type: "GET",
      dataType: "json",
      contentType: "application/json",
      beforeSend: function (xhr) {
        var accessToken = sessionStorage.getItem("accessToken");
        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      },
      success: function (cartItems) {
        // Check if cartItems is not empty
        if (cartItems.length > 0) {
          // Display cart items in the cartItemsContainer
          const cartItemsContainer = $("#cartItemsContainer");
          var cartContentHtml = "";

          cartItems.forEach(function (cartItem) {
            var itemPrice = cartItem.price * cartItem.quantity;
            totalPrice += itemPrice;

            // Check if the product is already in the productQuantities object
            if (productQuantities[cartItem.title]) {
              productQuantities[cartItem.title] += cartItem.quantity;
            } else {
              productQuantities[cartItem.title] = cartItem.quantity;
            }

            // Generate HTML for each product
            cartContentHtml += `
            <div class="row border border-warning rounded m-5 shadow-sm mt-2 cart-item" data-id="${
              cartItem.id
            }" data-price="${cartItem.price}">
              <div class="col p-2">
                <img src="" alt="${cartItem.title}" class="cart-item-image" />
              </div>
              <div class="col p-4 fs-5 cart-item-info">
              <p>
                <span class="cart-quantity">${cartItem.quantity} x</span> ${
              cartItem.title
            }
              </p>
                <div class="quantity-control">
                  <button class="btn btn-sm btn-warning minus-quantity">-</button>
                  <span class="quantity">${cartItem.quantity}</span>
                  <button class="btn btn-sm btn-warning ml-2 plus-quantity">+</button>
                  <button class="btn btn-sm btn-danger remove-item"><i class="fa-regular fa-trash-can"></i></button>
                </div>  
                
                  <p class="price">€ ${(
                    cartItem.price * cartItem.quantity
                  ).toFixed(2)}</p>
                   
              </div>
              <input type="hidden" name="productId" value="${
                cartItem.productId
              }" />

            </div>
          `;
          });

          // Append the subtotal at the bottom
          cartContentHtml += `
          <div class="row mx-5">
            <div class="col p-2">
              <p class="fs-3">Total: </p>
            </div>
            <div class="col p-2">
               <p class="fs-3" id="totalPrice">€ ${totalPrice.toFixed(2)}</p>
            </div>
          </div>
        `;

          cartContentHtml += `
          <div class="row mx-5 mb-5">
            <div class="col">
              <button class="btn btn-warning text-white shadow fs-5 ">
                <a href="shop.html" class="text-white text-decoration-none">Back to Shop</a>
              </button>
            </div>
            <div class="col">
              <button class="btn btn-success text-white shadow fs-5 ">
                <a href="order.html" class="text-white text-decoration-none">Checkout</a>
              </button>
            </div>
          </div>
        `;

          cartItemsContainer.append(cartContentHtml);

          // Call the function after displaying cart items
          updateCartItemQuantityAndPrice();
          fetchAndDisplayCartItemImages(cartItems);
          updateFooterClass();
        } else {
          // Handle the case where the cart is empty
          $("#cartItemsContainer").html("<p>Your cart is empty</p>");
          $(".footer").addClass("fixed-bottom");
        }
      },
      error: function (xhr, status, error) {
        // Handle errors, e.g., unauthorized or server error
        console.error("Error fetching cart items: " + error);
        // Optionally, display an error message to the user
      },
    });
  }

  function updateCartItemQuantityAndPrice() {
    $(".cart-item").each(function () {
      const cartItem = $(this);
      const minusBtn = cartItem.find(".minus-quantity");
      const plusBtn = cartItem.find(".plus-quantity");
      const quantityElement = cartItem.find(".quantity");
      const removeBtn = cartItem.find(".remove-item");
      const cartQuantityElement = cartItem.find(".cart-quantity");
      const priceElement = cartItem.find(".price");
      const pricePerItem = parseFloat(cartItem.data("price"));
      const cartItemId = cartItem.data("id");
      const productId = cartItem.find('input[name="productId"]').val();

      minusBtn.on("click", function () {
        let quantity = parseInt(quantityElement.text());
        if (quantity > 1) {
          quantity--;
          quantityElement.text(quantity);
          cartQuantityElement.text(`${quantity} x`);
          const totalPrice = (pricePerItem * quantity).toFixed(2);
          priceElement.text(`€ ${totalPrice}`);
          updateCartItem(cartItemId, quantity, productId);
          loadCartContent(userId, accessToken);

          updateTotalPrice();
        }
      });

      plusBtn.on("click", function () {
        let quantity = parseInt(quantityElement.text());
        quantity++;
        quantityElement.text(quantity);
        cartQuantityElement.text(`${quantity} x`);
        const totalPrice = (pricePerItem * quantity).toFixed(2);
        priceElement.text(`€ ${totalPrice}`);
        updateCartItem(cartItemId, quantity, productId);
        loadCartContent(userId, accessToken);

        updateTotalPrice();
      });

      removeBtn.on("click", function () {
        const cartItemId = cartItem.data("id");
        // Remove the item from the cart
        $.ajax({
          url: config.baseUrl + config.cartItem.delete + cartItemId,
          type: "DELETE",
          contentType: "application/json",
          beforeSend: function (xhr) {
            var accessToken = sessionStorage.getItem("accessToken");
            xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
          },
          success: function (response) {
            // Assuming your server returns a success response
            // Remove the HTML element for the cart item
            cartItem.remove();

            updateTotalPrice();
            // Check and update the footer class after removing an item

            // Call loadCartContent to update cart info immediately after cart data changes
            loadCartContent(userId, accessToken);

            showDeleteToast("Product successfully deleted from shopping cart.");
            const toast = new bootstrap.Toast(
              document.getElementById("toastContainer")
            );
            toast.show();
            updateFooterClass();
          },
          error: function (xhr, status, error) {
            // Handle errors, e.g., unauthorized or server error
            console.error("Error removing cart item: " + error);
            // Optionally, display an error message to the user
          },
        });
      });
    });
  }

  function updateTotalPrice() {
    let totalPrice = 0;
    $(".price").each(function () {
      const priceText = $(this).text();
      const price = parseFloat(priceText.replace("€ ", ""));
      if (!isNaN(price)) {
        totalPrice += price;
      }
    });
    $("#totalPrice").text(`€ ${totalPrice.toFixed(2)}`);
  }

  function fetchAndDisplayCartItemImages(cartItems) {
    cartItems.forEach(function (cartItem) {
      const imageReference = cartItem.img;
      const baseUrl = config.baseUrl;
      const filesEndpoint = config.file.files;
      const apiUrl = `${baseUrl}${filesEndpoint}/${imageReference}`;

      const imgElement = $(`[data-id="${cartItem.id}"] .cart-item-image`);

      fetch(apiUrl)
        .then((response) => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.blob();
        })
        .then((blob) => {
          const blobUrl = window.URL.createObjectURL(blob);
          imgElement.attr("src", blobUrl);
        })
        .catch((error) => {
          console.error("Fetch error:", error);
        });
    });
  }

  function updateCartItem(cartItemId, newQuantity, productId) {
    const updateUrl = config.baseUrl + config.cartItem.update;
    const accessToken = sessionStorage.getItem("accessToken");
    const userId = sessionStorage.getItem("userId");

    const requestData = {
      id: cartItemId,
      productId: productId,
      userId: userId,
      quantity: newQuantity,
    };

    $.ajax({
      url: updateUrl,
      type: "PUT",
      data: JSON.stringify(requestData),
      dataType: "json",
      contentType: "application/json",
      beforeSend: function (xhr) {
        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      },
      success: function () {
        // Call loadCartContent to update cart info immediately after cart data changes
        loadCartContent(userId, accessToken);
      },
      error: function (xhr, status, error) {
        console.error("Error updating cart item: " + error);
      },
    });
  }
});
