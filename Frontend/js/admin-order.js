import config from "./config.js";

$(document).ready(function () {
  $(".footer").addClass("fixed-bottom");

  $(document).on("click", "#showSearchOrder", function (event) {
    $("#searchOrderResult tbody").empty();

    const email = $("#email").val();
    const orderId = $("#order-id").val();
    const orderDate = $("#order-date").val();
    const orderStatus = $("#order-status-search").val();

    const filters = {};

    if (email) {
      filters["email"] = email;
    }

    if (orderId) {
      filters["orderId"] = orderId;
    }

    if (orderDate) {
      const dateObj = new Date(orderDate);
      const formattedDate = `${String(dateObj.getDate()).padStart(
        2,
        "0"
      )}.${String(dateObj.getMonth() + 1).padStart(
        2,
        "0"
      )}.${dateObj.getFullYear()}`;

      filters["orderDate"] = formattedDate;
    }

    if (orderStatus) {
      filters["orderStatus"] = orderStatus;
    }

    const filterJSON = JSON.stringify(filters);

    $.ajax({
      url: config.baseUrl + config.order.search,
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      beforeSend: function (xhr) {
        var accessToken = sessionStorage.getItem("accessToken");

        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      },
      data: filterJSON,
      success: function (orders) {
        addOrders(orders);
        if (orders.length > 2) {
          $(".footer").removeClass("fixed-bottom");
        } else {
          $(".footer").addClass("fixed-bottom");
        }
      },
      error: function (error) {
        console.error(error);
      },
    });
  });

  // Load Search Result

  function addOrders(orders) {
    const tableBody = $("#searchOrderResult tbody");
    tableBody.empty();

    orders.forEach(function (order) {
      const row = $("<tr>");
      row.append($("<td>").text(order.id));
      row.append($("<td>").text(order.userId));
      row.append($("<td>").text(order.firstName));
      row.append($("<td>").text(order.lastName));
      row.append($("<td>").text(order.email));
      const orderDate = new Date(order.orderDate);
      const formattedDate = orderDate.toLocaleDateString("de-AT", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      });

      const formattedTime = orderDate.toLocaleTimeString("de-AT", {
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
      });

      row.append($("<td>").text(formattedDate + " " + formattedTime));
      const statusText = getStatusText(order.status);
      row.append($("<td>").text(statusText));
      row.append(
        $("<td>").html(
          `<button class="btn btn-outline-warning editOrder" value="${order.id}">edit</button>`
        )
      );
      tableBody.append(row);
    });
  }

  // Load Edit Form

  $(document).on("click", ".editOrder", function (event) {
    $("#editFormContainer").css("display", "block");
    $(".footer").removeClass("fixed-bottom");

    const orderId = $(this).val();

    $.ajax({
      url: config.baseUrl + config.order.findById + orderId,
      type: "GET",
      cors: true,
      beforeSend: function (xhr) {
        var accessToken = sessionStorage.getItem("accessToken");
        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      },
      success: function (orderData) {
        fillEditForm(orderData);
      },
      error: function (error) {
        console.error(error);
      },
    });
  });

  function fillEditForm(orderData) {
    $("#order-id-edit").val(orderData.id);
    const orderDate = new Date(orderData.orderDate);
    const formattedDate = orderDate.toLocaleDateString("de-AT", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });

    $("#order-date-edit").val(formattedDate);
    $("#order-status-edit").val(orderData.status);

    $("#first-name-edit").val(orderData.firstName);
    $("#last-name-edit").val(orderData.lastName);
    $("#street-edit").val(orderData.address);
    $("#zip-edit").val(orderData.zip);
    $("#city-edit").val(orderData.city);

    const tableBody = $("#addEditOrder tbody");
    tableBody.empty();

    let totalSum = 0;
    orderData.orderItems.forEach(function (item) {
      const row = $("<tr>");
      row.append($("<td>").text(item.productId));
      row.append($("<td>").text(item.title));
      row.append($("<td>").text(item.price));
      row.append($("<td>").text(item.quantity));
      tableBody.append(row);

      totalSum += item.price * item.quantity;
    });

    const sumRow = $("<tr>");
    sumRow.append($("<td>").text(""));
    sumRow.append($("<td>").text("Sum").addClass("fw-bold"));
    sumRow.append(
      $("<td>")
        .text("€ " + totalSum.toFixed(2))
        .addClass("fw-bold")
    );
    sumRow.append($("<td>").text(""));
    tableBody.append(sumRow);

    $("#addEditOrder").show();
  }

  $("#saveEditProduct").on("click", function () {
    const orderId = $("#order-id-edit").val();
    const orderStatus = $("#order-status-edit").val();

    const updatedOrder = {
      id: orderId,
      status: orderStatus,
    };

    $.ajax({
      url: config.baseUrl + config.order.update,
      method: "PUT",
      dataType: "json",
      contentType: "application/json",
      beforeSend: function (xhr) {
        var accessToken = sessionStorage.getItem("accessToken");
        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      },
      data: JSON.stringify(updatedOrder),
      success: function (response) {
        clearToasts();
        showSuccessToast("Updated successfully!");

        // Display the toast message
        const toast = new bootstrap.Toast(
          document.getElementById("toastContainer")
        );
        toast.show();
        // Reload the page after a delay (e.g., 2 seconds)
        setTimeout(function () {
          location.reload();
        }, 2000);
        $(".footer").addClass("fixed-bottom");
      },
      error: console.error,
    });
  });

  function getStatusText(status) {
    switch (status) {
      case "IN_PROGRESS":
        return "In Progress";
      case "SHIPPED":
        return "Shipped";
      case "DELIVERED":
        return "Delivered";
      case "CANCELED":
        return "Canceled";
      default:
        return "";
    }
  }
});
