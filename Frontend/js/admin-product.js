$(document).ready(function () {
  //TOOGLE FOR CREATE PRODUCT FORM
  $("#showNewProduct").click(function () {
    $("#createNewProduct").toggle();
  });

  //CREATE NEW PRODUCT
  $("#createProductButton").on("click", (_e) => {
    //Validation open
    isActive = $("#isActive").is(":checked") ? true : false;
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
    
    $.ajax({
      url: "http://localhost:8080/api/products/create",
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      beforeSend: function (xhr) {
        var accessToken = sessionStorage.getItem("accessToken");
        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      },
      data: JSON.stringify(product),
      success: console.log,
      error: console.error,
    });
  });

  //LOAD CATEGORIES FROM DATABASE  & generate Dropdown values in new product form
  $.ajax({
    url: "http://localhost:8080/api/categories/isActive/true",
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

  //SEARCH FUNCTION (dzt nur nach title möglich ohne aktiv!)
  $(document).on("click", "#showSearchProduct", function (event) {
    const searchId = $("#product-id").val();
    const search = $("#product-title-search").val();

    $.ajax({
      url: "http://localhost:8080/api/products/searchproduct/" + search,
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

  //ADD SEARCHED PRODUCTS FROM DATABASE TO LIST
  function addProducts(products) {
    const allSearchedProducts = $("#searchResult");
    allSearchedProducts.empty();

    for (let product of products) {
      allSearchedProducts.append(createProduct(product));
    }
  }

  function createProduct(product) {
    const searchedProduct = $(`<tr>
      <td scope="col">${product.id}</td>
      <td scope="col">${product.title ? product.title : ""}</td>
      <td scope="col">${
        product.category && product.category.title ? product.category.title : ""
      }</td>
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
  
    // Zuerst die Kategorien laden
    $.ajax({
      url: "http://localhost:8080/api/categories/isActive/true",
      type: "GET",
      cors: true,
      success: function (categories) {
        // Dann die Produktinformationen laden und bearbeiten
        $.ajax({
          url: "http://localhost:8080/api/products/" + id,
          type: "GET",
          cors: true,
          success: function (product) {
            console.log(product);
            editProducts(product, categories); // Übergebe die geladenen Kategorien an die editProducts-Funktion
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
  });
  
  function editProducts(product, categories) {
    const addEditProduct = $("#addEditProduct");
    addEditProduct.empty();
  
    // Weitere Code-Logik für die Bearbeitung der Kategorien und Produkte
    if (product.active == true) {
      productedit = "checked";
    } else {
      productedit = "";
    }
  
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
                    <input id="product-id-edit" type="text" class="form-control" name="product-id" value="${product.id}" required disabled>
                  </div>
                </div>
                <div class="col-md-5">
                  <div class="form-group">
                    <label for="product-name" class="fs-5">Product Name</label>
                    <input id="product-name" type="text" class="form-control" name="product-name" value="${product.title}" required>
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
                    </div>
                  </div>
                </div>
              </div>
              <div class="row mb-3">
              <div class="col-md-12">
                <div class="form-group">
                  <label for="product-description" class=" fs-5">Product Description</label>
                  <textarea class="form-control" id="product-description-edit" rows="3" >${product.description}</textarea>
                </div>
              </div>
            </div>
  
            <div class="row mb-3">
  
              <div class="col-md-4">
                <div class="form-group">
                  <label for="product-price" class="fs-5">Product Price</label>
                  <input id="product-price-edit" type="text" class="form-control " name="product-price" value="${product.price}" required>
  
  
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label for="product-stock" class="   fs-5">Product Stock</label>
                  <input id="product-stock-edit" type="text" class="form-control " name="product-stock" value="${product.stock}" required>
  
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
              <div class="col-md-12">
              <div class="form-group">
              <label for="product-img" class="fs-5">Product Image Url</label>
              <input id="product-img-edit" type="text" class="form-control " name="product-img-edit" value="${product.img}" required>
              </div>
              </div>
            </div>              <button type="button" class="btn btn-warning text-white float-end mt-2 mb-2" id="saveEditProduct"> save</button>
            </form>
          </div>
        </div>
      </div>`);
  
    addEditProduct.append(editProduct);
  }
  
  function createCategoryOptions(categories, selectedCategoryId) {
    let optionsHtml = '';
    for (let category of categories) {
      const selected = category.id === selectedCategoryId ? 'selected' : '';
      optionsHtml += `<option value="${category.id}" ${selected}>${category.title}</option>`;
    }
    return optionsHtml;
  }
  
  //EDIT PRODUCT

  $(document).on("click", "#saveEditProduct", function (event) {
   
    isActive = $(".status").is(":checked") ? true : false;

    const product = {
      id: $("#product-id-edit").val(),
      title: $("#product-name").val(),
      description: $("#product-description-edit").val(),
      price: $("#product-price-edit").val(),
      stock: $("#product-stock-edit").val(),
      img: $("#product-img-edit").val(),
      categoryId: $("#product-category-edit").val(),
      //categoryObject[0],
      //Check if is checked --> value = 1 /0
      active: isActive,
    };

    console.log(product);
    $.ajax({
      url: "http://localhost:8080/api/products/update",
      type: "PUT",
      dataType: "json",
      contentType: "application/json",
      beforeSend: function (xhr) {
        var accessToken = sessionStorage.getItem("accessToken");
        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      },
      data: JSON.stringify(product),
      success: console.log,
      error: console.error,
    });
  });

  //DELETE PRODUCT
  $(document).on("click", ".delete", function (event) {
    const deleteId = event.target.value;

    //console.log(deleteId);

    $.ajax({
      url: "http://localhost:8080/api/products/delete/" + deleteId,
      type: "DELETE",
      dataType: "text",
      contentType: "application/json",
      beforeSend: function (xhr) {
        var accessToken = sessionStorage.getItem("accessToken");
        xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      },
      success: function (response) {
        console.log("Successfully deleted:", response);
        //showToast("Produkt erfolgreich gelöscht", "success");
        location.reload();
      },
      error: function (xhr, textStatus, error) {
        console.error("Error deleting:", error);
      },
    });
  });
});
