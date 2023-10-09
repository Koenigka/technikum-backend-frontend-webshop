$(document).ready(function () {
  // Event-Handler für das Offcanvas-Modal öffnen
  $("#offcanvasNavbar").on("show.bs.offcanvas", function () {
    var userId = sessionStorage.getItem("userId");
    var accessToken = sessionStorage.getItem("accessToken");

    loadCartContent(userId, accessToken);
    console.log("cartcontent goes here");
  });

  function loadCartContent(userId, accessToken) {
    var totalPrice = 0;

    $.ajax({
      url: "http://localhost:8080/api/cart/myCart" + "?userId=" + userId,
      type: "GET",
      beforeSend: function (xhr) {
        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      },
      success: function (data) {
        var cartContentHtml = "";

        data.forEach(function (cartItem) {
          var itemPrice = cartItem.price * cartItem.quantity;
          totalPrice += itemPrice;
          cartContentHtml += `


                <div class="row border border-warning rounded shadow-sm mt-2 cart-item" data-id="${
                  cartItem.id
                }">
                        <div class="col p-2">              
                        <img src="" alt="${
                          cartItem.title
                        }" class="cart-item-image" />
                        </div>
                        <div class="col p-2 cart-item-info">
                            <p>${cartItem.title}</p>
                            <p>€  ${cartItem.price * cartItem.quantity}</p>
                            <div class="col p-2 qty mt-5 mb-3 counter">
                            <span class="minus bg-light fs-3">-</span>
                            <input type="number" class="count" name="qty" value="${
                              cartItem.quantity
                            }" />
                            <span class="plus bg-light fs-3">+</span>
                        </div>
                        <p><button class="btn btn-outline-danger btn-sm">delete</button></p>
                        </div>
                    </div>
                    
                `;
        });

        cartContentHtml += `
            <div class="row">
                <div class="col p-2">
                    <p class="fs-3">Sum: </p>
                </div>
                <div class="col p-2">
                    <p class="fs-3">$${totalPrice.toFixed(2)}</p>
                </div>
            </div>
        `;

        $(".offcanvas-body").html(cartContentHtml);

        fetchAndDisplayCartItemImages(data); 
      },
      error: function (error) {
        console.error("Error loading cart content: " + error);
      },
    });
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
