$(document).ready(function () {
  if (sessionStorage.getItem("accessToken")) {
    // User is logged in
    const userId = sessionStorage.getItem("userId");

    $.ajax({
      url: `http://localhost:8080/api/users/${userId}`,
      type: "GET",
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`, // Include the JWT token
      },
      success: function (userData) {
        // Call the function to display user details
        fetchAndDisplayCartItems(userId);
      },
      error: function (xhr, status, error) {
        // Handle errors, such as user not found
        console.error("Error fetching user data: " + error);
      },
    });
  } else {
    // User is not logged in
    // Handle the case where user data is not available
    console.log("User data not found.");
  }

  function fetchAndDisplayCartItems(userId) {
    var totalPrice = 0;
    const productQuantities = {}; // To keep track of product quantities
    // Make an AJAX request to fetch cart items
    $.ajax({
      url: "http://localhost:8080/api/cart/myCart" + "?userId=" + userId, // Update the URL as needed
      type: "GET",
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
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
                <img src="http://localhost:8080/api/files/${
                  cartItem.img
                }" alt="${cartItem.title}" class="cart-item-image" />
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
        } else {
          // Handle the case where the cart is empty
          $("#cartItemsContainer").html("<p>Your cart is empty</p>");
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

      minusBtn.on("click", function () {
        let quantity = parseInt(quantityElement.text());
        if (quantity > 1) {
          quantity--;
          quantityElement.text(quantity);
          cartQuantityElement.text(`${quantity} x`);
          const totalPrice = (pricePerItem * quantity).toFixed(2);
          priceElement.text(`€ ${totalPrice}`);
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
        updateTotalPrice();
      });
      removeBtn.on("click", function () {
        const cartItemId = cartItem.data("id");

        // Remove the item from the cart
        $.ajax({
          url: "http://localhost:8080/api/cart/remove?cartItemId=" + cartItemId,
          type: "DELETE",
          dataType: "text",
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
            showDeleteToast("Product successfully deleted from shopping cart.");
            const toast = new bootstrap.Toast(
              document.getElementById("toastContainer")
            );
            toast.show();
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
});
