import config from "./config.js";

// Function to close the edit window
function closeEditWindow() {
  const addEditProduct = $("#addEditProduct");
  addEditProduct.empty();
  $(".footer").addClass("fixed-bottom");
}

$(document).ready(function () {
  //TOOGLE FOR CREATE PRODUCT FORM
  $("#showNewProduct").click(function () {
    var accessToken = sessionStorage.getItem("accessToken");
    if (!accessToken) {
      // User is not logged in, redirect to login page with a message
      window.location.href =
        "login.html?message=You are not logged in! Please first log in and then you can continue your action.";
    } else {
      $("#createNewProduct").toggle();

      const fileInput = document.getElementById("product-image");

      fileInput.addEventListener("change", function () {
        const selectedFile = fileInput.files[0];

        if (selectedFile) {
          const blobUrl = URL.createObjectURL(selectedFile);
          $("#product-image-preview-create").attr("src", blobUrl);
        } else {
          $("#product-image-preview-create").attr("src", "");
        }
      });
      
    }
  });

  function validateProduct(product, createForm) {
    $(".input-error").text("");

    // Initialize isValid as true
    let isValid = true;

    if (!product.title || product.title.trim() === "") {
      // Validate input values
      isValid = false;
      $(`#titleError${createForm}`).text("Title is required.");
      $("#titleError-edit").text("Title is required.");
    }

    if (!product.description || product.description.trim() === "") {
      isValid = false;
      $(`#descriptionError${createForm}`).text("Description is required.");
      $("#descriptionError-edit").text("Description is required.");
    }
    if (!product.price || product.price.trim() === "") {
      isValid = false;
      $(`#priceError${createForm}`).text("Price is required.");
      $("#priceError-edit").text("Price is required.");
    } else if (isNaN(product.price) || parseFloat(product.price) <= 0) {
      isValid = false;
      $(`#priceError${createForm}`).text("Price must be a positive number.");
      $("#priceError-edit").text("Price must be a positive number.");
    }

    if (!product.stock || product.stock.trim() === "") {
      isValid = false;
      $(`#stockError${createForm}`).text("Stock is required.");
      $("#stockError-edit").text("Stock is required.");
    } else if (isNaN(product.stock) || parseInt(product.stock) < 0) {
      isValid = false;
      $(`#stockError${createForm}`).text(
        "Stock must be a non-negative number."
      );
      $("#stockError-edit").text("Stock must be a non-negative number.");
    }

    if (
      product.categoryId === "" ||
      !Number.isInteger(Number(product.categoryId))
    ) {
      isValid = false;
      $(`#categoryIdError${createForm}`).text("Category is required.");
      $("#categoryIdError-edit").text("Category is required.");
    }
    // Validate the image URL
    const imageUrlPattern = /(\.jpg|\.jpeg|\.png|\.gif)$/i;
    if (!product.img) {
      isValid = false;
      $(`#imgError${createForm}`).text("Image URL is required.");
      $("#imgError-edit").text("Image URL is required.");
    } else if (!imageUrlPattern.test(product.img)) {
      isValid = false;
      $(`#imgError${createForm}`).text("Invalid image URL format.");
      $("#imgError-edit").text("Invalid image URL format.");
    }
    return isValid;
  }

 

  //CREATE NEW PRODUCT
  $("#createProductButton").on("click", (_e) => {
    event.preventDefault();

    const isActive = $("#isActive").is(":checked") ? true : false;
    const product = {
      title: $("#product-title").val(),
      description: $("#product-description").val(),
      price: $("#product-price").val(),
      stock: $("#product-stock").val(),
      img: $("#product-img").val(),
      categoryId: $("#product-category").val(),
      //Check if is checked --> value = true/false
      active: isActive,
    };

    const fileInput = document.getElementById("product-image");
    const selectedFile = fileInput.files[0];

    if (selectedFile) {     

      const formData = new FormData();
      formData.append("file", selectedFile);

      $.ajax({
        url: config.baseUrl + config.file.files,
        type: "POST",
        data: formData,
        contentType: false,
        processData: false,
        beforeSend: function (xhr) {
          var accessToken = sessionStorage.getItem("accessToken");
          xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
        },
        success: function (response) {
          const imageReference = response.reference;
          product.img = imageReference;

          const isValid = validateProduct(product, "");

          if (isValid) {
            $.ajax({
              url: config.baseUrl + config.product.create,
              type: "POST",
              dataType: "json",
              contentType: "application/json",
              beforeSend: function (xhr) {
                var accessToken = sessionStorage.getItem("accessToken");
                xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
              },
              data: JSON.stringify(product),
              success: function (response) {
                clearToasts();
                showSuccessToast("New product successfully created!");
                // Display the toast message
                const toast = new bootstrap.Toast(
                  document.getElementById("toastContainer")
                );
                toast.show();
                $("#createNewProduct").hide();
                // Reload the page after a delay (e.g., 2 seconds)
                setTimeout(function () {
                  location.reload();
                }, 2000);
              },
              error: console.error,
            });
          } else {
            console.log("Please select an image.");
          }
        },
        error: console.error,
      });
    } else {
      console.log("Please select an image.");
    }
  });

  //LOAD CATEGORIES FROM DATABASE  & generate Dropdown values in new product form
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
    const allCategories = $(".product-category");
    allCategories.empty();
    const selectNone = `<option value="">please choose</option>`;
    allCategories.append(selectNone);
    for (let category of categories) {
      allCategories.append(createCategory(category));
    }
  }

  function createCategory(category) {
    const select = `<option value='${category.id}'>${category.title}</option>`;
    return select;
  }

  //SEARCH FUNCTION
  $(document).on("click", "#showSearchProduct", function (event) {
    const searchTitle = $("#product-title-search").val();
    const searchCategory = $("#product-category-search").val();
    const isActive = $("input[name='status']:checked").val();

    const filters = {};

    if (searchTitle) {
      filters["filter[productTitle]"] = searchTitle;
    }

    if (searchCategory) {
      filters["filter[categoryId]"] = searchCategory;
    }

    if (isActive !== undefined) {
      filters["filter[active]"] = isActive;
    }

    const filterJSON = JSON.stringify(filters);
    console.log(filterJSON);

    // Close the edit window if it's open
    closeEditWindow();

    $.ajax({
      url: config.baseUrl + config.product.search,
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      beforeSend: function (xhr) {
        var accessToken = sessionStorage.getItem("accessToken");
        if (!accessToken) {
          // User is not logged in, redirect to login page with a message
          window.location.href =
            "login.html?message=You are not logged in! Please first log in and then you can continue your action.";
        } else {
          xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
        }
      },
      data: filterJSON,
      success: function (products) {
        addProducts(products);
      },
      error: function (error) {
        console.error(error);
      },
    });
  });

  //ADD SEARCHED PRODUCTS FROM DATABASE TO LIST
  function addProducts(products) {
    const selectedStatus = $("input[name='status']:checked").val();
    const statusMessage = selectedStatus === "true" ? "active" : "inactive";
    const allSearchedProducts = $("#searchResult");
    allSearchedProducts.empty();

    if (products.length === 0) {
      const isActive = $("#search-status").prop("checked");
      clearToasts();
      showErrorToast(`No ${statusMessage} products found.`);
      // Display a message when no users are found
      const toast = new bootstrap.Toast(
        document.getElementById("toastContainer")
      );
      toast.show();
      return;
    }

    for (let product of products) {
      allSearchedProducts.append(createProduct(product));
    }
  }

  function createProduct(product) {
    console.log("In createProduct-Funktion. Product:", product);
    const searchedProduct = $(`<tr>
      <td scope="col">${product.id}</td>
      <td scope="col">${product.title ? product.title : ""}</td>     
      <td scope="col">${product.price ? product.price : ""}</td>
      <td scope="col">${product.stock ? product.stock : ""}</td>
      <td scope="col"><button class="btn btn-outline-warning editProduct" value="${
        product.id
      }">edit</button></td>
      <td scope="col">
      <button class="btn btn-outline-danger delete" value="${
        product.id
      }">delete</button>  
   </td>
    </tr>`);
    $(".footer").removeClass("fixed-bottom");
    return searchedProduct;
  }

  //LOAD PRODUCT TO EDIT FORM
  $(document).on("click", ".editProduct", function (event) {
    const id = event.target.value;
    console.log(id);

    // Scroll to the top of the edit window
    $("html, body").animate(
      {
        scrollTop: $("#addEditProduct").offset().top,
      },
      500
    ); // You can adjust the scroll speed (500ms in this example)
    // Generate a unique ID for the edit form container
    const editFormContainerId = `editFormContainer-${id}`;
    const editFormContainer = $(`#${editFormContainerId}`);

    if (editFormContainer.length === 0) {
      // If the container doesn't exist, create it
      const addEditProduct = $("#addEditProduct");
      addEditProduct.empty();

      // Zuerst die Kategorien laden
      $.ajax({
        url: config.baseUrl + config.category.findByActive,
        type: "GET",
        cors: true,
        success: function (categories) {
          console.log("load categories");
          // Dann die Produktinformationen laden und bearbeiten
          $.ajax({
            url: config.baseUrl + config.product.findById + id,
            type: "GET",
            cors: true,
            success: function (product) {
              editProducts(product, categories, editFormContainer); // Pass the container to editProducts function
            },
            error: function (error) {
              console.error(error);
            },
          });
        },
        error: function (error) {
          console.error(error);
        },
      });
    }
  });
  const addEditProduct = $("#addEditProduct");
  addEditProduct.empty();

  function editProducts(product, categories) {
    // Weitere Code-Logik für die Bearbeitung der Kategorien und Produkte
    if (product.active == true) {
      var productedit = "checked";
    } else {
      var productedit = "";
    }

    // Load Img from FileServer
    const imageReference = product.img;
    const baseUrl = config.baseUrl;
    const filesEndpoint = config.file.files;
    const apiUrl = `${baseUrl}${filesEndpoint}/${imageReference}`;

    const accessToken = sessionStorage.getItem("accessToken");

    fetch(apiUrl, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.blob();
      })
      .then((blob) => {
        const blobUrl = window.URL.createObjectURL(blob);
        $("#product-image-preview").attr("src", blobUrl);
        $("#original-product-img").val(imageReference);

      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });

    const editProduct = $(`
      <div class="container rounded mt-5 border border-warning bg-light shadow-lg">
        <p class="fs-4 fw-bold pt-2">Edit Product</p>
        <div class="row">
          <div class="col">
            <form>
              <div class="row mt-3 mb-3">
                <div class="col-md-2">
                  <div class="form-group">
                    <label for="product-id" class="fs-5">Product Id</label>
                    <input id="product-id-edit" type="text" class="form-control" name="product-id" value="${
                      product.id
                    }" required disabled>
                  </div>
                </div>
                <div class="col-md-5">
                  <div class="form-group">
                    <label for="product-name" class="fs-5">Product Name</label>
                    <input id="product-name" type="text" class="form-control" name="product-name" value="${
                      product.title
                    }" required>
                    <p class="input-error" id="titleError-edit" style="color: red"></p>
                  </div>
                </div>
                <div class="col-md-5">
                  <div class="col-md-6">
                    <div class="form-group">
                      <label for="product-category" class="fs-5">Product Category</label>
                      <!-- Load Categories -->
                      <select name="product-category" class="form-select fs-5" id="product-category-edit" required>
                        ${createCategoryOptions(categories, product.categoryId)}
                      </select>
                      <p class="input-error" id="categoryIdError-edit" style="color: red"></p>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row mb-3">
              <div class="col-md-12">
                <div class="form-group">
                  <label for="product-description" class=" fs-5">Product Description</label>
                  <textarea class="form-control" id="product-description-edit" rows="3" >${
                    product.description
                  }</textarea>
                  <p class="input-error" id="descriptionError-edit" style="color: red"></p>
                </div>
              </div>
            </div>
  
            <div class="row mb-3">
  
              <div class="col-md-4">
                <div class="form-group">
                  <label for="product-price" class="fs-5">Product Price</label>
                  <input id="product-price-edit" type="text" class="form-control " name="product-price" value="${
                    product.price
                  }" required>
                  <p class="input-error" id="priceError-edit" style="color: red"></p>
  
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label for="product-stock" class="   fs-5">Product Stock</label>
                  <input id="product-stock-edit" type="text" class="form-control " name="product-stock" value="${
                    product.stock
                  }" required>
                  <p class="input-error" id="stockError-edit" style="color: red"></p>
                </div>
              </div>
              <div class="col-md-4 d-flex align-items-end">
                <div class="form-check mb-2 ">
                <div class="form-group">
                  <input type="checkbox" class="form-check-input status" name="status"  ${productedit}>
                  <label class="form-check-label fs-5" for="status">
                    active
                  </label>
                </div>
                </div>
              </div>
  
            </div>          
            <div class="row mb-3">  
            <input type="hidden" id="original-product-img" value="">           
              <div class="col-md-6">
                <label class="fs-5">Product Image Upload</label>
                <input type="file" class="form-control" id="product-image-file-edit">
              </div>
              <div class="col-md-6">
              <label class="fs-5">Product Image Preview</label>
              <img class="mt-3" id="product-image-preview" src="" alt="Product Image" width="200" height="200">
            </div>
            </div>
            <button type="button" class="btn btn-warning text-white float-end mt-2 mb-2" id="saveEditProduct"> save</button>
            </form>
          </div>
        </div>
      </div>`);

    addEditProduct.append(editProduct);

    // Change Preview of img if new file is uploaded

    const fileInputEdit = document.getElementById("product-image-file-edit");
    const imagePreviewEdit = document.getElementById("product-image-preview");

    fileInputEdit.addEventListener("change", function () {
      const newFile = fileInputEdit.files[0];

      if (newFile) {
        const blobUrl = URL.createObjectURL(newFile);
        console.log(blobUrl);
        imagePreviewEdit.src = blobUrl;
      }
    });
  }

  function createCategoryOptions(categories, selectedCategoryId) {
    let optionsHtml = "";
    for (let category of categories) {
      const selected = category.id === selectedCategoryId ? "selected" : "";
      optionsHtml += `<option value="${category.id}" ${selected}>${category.title}</option>`;
    }
    return optionsHtml;
  }

  //EDIT PRODUCT

  $(document).on("click", "#saveEditProduct", function (event) {
    const isActive = $(".status").is(":checked") ? true : false;

    const product = {
      id: $("#product-id-edit").val(),
      title: $("#product-name").val(),
      description: $("#product-description-edit").val(),
      price: $("#product-price-edit").val(),
      stock: $("#product-stock-edit").val(),
      categoryId: $("#product-category-edit").val(),
      active: isActive,
    };

    const fileInputEdit = document.getElementById("product-image-file-edit");
    const newFile = fileInputEdit.files[0];

    if (newFile) {
      const formDataEdit = new FormData();
      formDataEdit.append("file", newFile);

      $.ajax({
        url: config.baseUrl + config.file.files,
        type: "POST",
        data: formDataEdit,
        contentType: false,
        processData: false,
        beforeSend: function (xhr) {
          var accessToken = sessionStorage.getItem("accessToken");
          xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
        },
        success: function (response) {
          const imageReferenceEdit = response.reference;
          product.img = imageReferenceEdit;
          saveEditedProduct(product);
        },
        error: console.error,
      });

      // Todo - delete old File from server 
    }
    else {
      product.img = $("#original-product-img").val();
      saveEditedProduct(product); 
    }
  });
  

  // Validate the product and get the result
  //const isValid = validateProduct(product, "-edit");

  function saveEditedProduct(product) {
    $.ajax({
      url: config.baseUrl + config.product.update,
      type: "PUT",
      dataType: "json",
      contentType: "application/json",
      beforeSend: function (xhr) {
        var accessToken = sessionStorage.getItem("accessToken");
        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      },
      data: JSON.stringify(product),
      success: function (response) {
        clearToasts();
        showSuccessToast("Updated successfully!");
        // Display the toast message
        const toast = new bootstrap.Toast(
          document.getElementById("toastContainer")
        );
        toast.show();
        $("#addEditProduct").empty(); // Clear the edit product form
      },
      error: console.error,
    });
  }
});

//DELETE PRODUCT
$(document).on("click", ".delete", function (event) {
  const deleteId = event.target.value;

  // Show the delete confirmation modal
  $("#deleteProductModal").modal("show");

  // When the delete button in the modal is clicked, perform the deletion
  // Todo - delete old file from Server
  $("#confirmDelete").click(function () {
    $.ajax({
      url: config.baseUrl + config.product.delete + deleteId,
      type: "DELETE",
      dataType: "text",
      contentType: "application/json",
      beforeSend: function (xhr) {
        var accessToken = sessionStorage.getItem("accessToken");
        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      },
      success: function (response) {
        console.log("Successfully deleted:", response);
        // Show a toast message
        showDeleteToast("Product deleted successfully.");
        const toast = new bootstrap.Toast(
          document.getElementById("toastContainer")
        );
        toast.show();
        // Hide the modal after deletion
        $("#deleteProductModal").modal("hide");
        // Reload the page after a delay (e.g., 2 seconds)
        setTimeout(function () {
          location.reload();
        }, 2000);
      },
      error: function (xhr, textStatus, error) {
        console.error("Error deleting:", error);
      },
    });
  });
});
