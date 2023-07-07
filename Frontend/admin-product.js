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
      url: "http://localhost:8080/api/products",
      type: "POST",
      cors: true,
      contentType: "application/json",
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
    <td scope="col">${product.title}</td>
    <td scope="col">${product.category.title}</td>
    <td scope="col">${product.price}</td>
    <td scope="col">${product.stock}</td>
    <td scope="col"><button class="btn btn-outline-warning editProduct" value="${product.id}">edit</button></td>
    <td scope="col">
    <button class="btn btn-outline-danger delete" value="${product.id}">delete</button>  
 </td>
  </tr>`);
    return searchedProduct;
  }

  //LOAD PRODUCT TO EDIT FORM
  $(document).on("click", ".editProduct", function (event) {
    const id = event.target.value;
    //console.log(id);

    $.ajax({
      url: "http://localhost:8080/api/products/" + id,
      type: "GET",
      cors: true,
      success: function (product) {
        editProducts(product);
      },
      error: function (error) {
        console.error(error);
      },
    });

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

    const addEditProduct = $("#addEditProduct");
    addEditProduct.empty();

    function editProducts(product) {
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


          <div class="row mt-3 mb-3 ">
            <div class="col-md-2">
              <div class="form-group">
                <label for="product-id" class=" fs-5">Product Id</label>
                <input id="product-id-edit" type="text" class="form-control " name="product-id" value="${product.id}" required disabled>
              </div>
            </div>
            <div class="col-md-5">
              <div class="form-group">
                <label for="product-name" class=" fs-5">Product Name</label>
                <input id="product-name" type="text" class="form-control " name="product-name" value="${product.title}" required>
              </div>
            </div>
            <div class="col-md-5">
            <div class="col-md-6">
            <div class="form-group">
              <label for="product-category" class="fs-5">Product Category</label>
              <!-- Load Categories -->
              <select name="product-category" class="form-select fs-5" id="product-category-edit" required>
              <option value='${product.category.id}'>${product.category.title}</option>              
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
          </div>
          <button type="button" class="btn btn-warning text-white float-end mt-2 mb-2" id="saveEditProduct"> save</button>

        </form>
      </div>
    </div>
    </div>`);

      addEditProduct.append(editProduct);
    }

    // LOAD CATEGORIES FOR EDIT
    //OFFEN: die gewählte kommt zur Zeit doppelt vor

    function addCategories(categories) {
      const allCategories = $("#product-category-edit");

      //console.log(allCategories);

      for (let category of categories) {
        allCategories.append(createCategory(category));
      }
    }

    function createCategory(category) {
      const select = `<option value='${category.id}'>${category.title}</option>`;
      return select;
    }

    $(".footer").removeClass("fixed-bottom");
  });

  //EDIT PRODUCT

  $(document).on("click", "#saveEditProduct", function (event) {
    const id = $("#product-id-edit").val();
    console.log(id);
    isActive = $(".status").is(":checked") ? true : false;

    const product = {
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
      url: "http://localhost:8080/api/products/" + id,
      type: "PUT",
      cors: true,
      contentType: "application/json",
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
      url: "http://localhost:8080/api/products/" + deleteId,
      type: "DELETE",
      cors: true,
      success: console.log,
      error: console.error,
    });
  });
});
