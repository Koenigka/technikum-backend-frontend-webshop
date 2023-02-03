$(document).ready(function () {
  //LOAD "NEW" PRODUCT FORM
  $("#showNewProduct").click(function () {
    $("#createNewProduct").toggle();
  });

  //CREATE NEW PRODUCT
  $("#createProductButton").on("click", (_e) => {
    /*const categoryObject = [];
    const selectedCategory = $("#product-category").val();
    for (let item of allCategoriesArray) {
      if (item.id == selectedCategory) {
        categoryObject.push(item);
      }
    }*/

    //Validation fehlt!!!
    const product = {
      title: $("#product-title").val(),
      description: $("#product-description").val(),
      price: $("#product-price").val(),
      stock: $("#product-stock").val(),
      img: $("#product-img").val(),
      categoryId: $("#product-category").val(),
      //categoryObject[0],
      //Check if is checked --> value = 1 /0
      isActive: "true",
    };
    $.ajax({
      url: "http://localhost:8080/products",
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
    url: "http://localhost:8080/categories",
    type: "GET",
    cors: true,
    success: function (categories) {
      addCategories(categories);
    },
    error: function (error) {
      console.error(error);
    },
    
  });

  
  //Speichert alle Kategorie-Objekte um das gewählte dann im neuen Produkt übergeben zu können
  //const allCategoriesArray = [];

  function addCategories(categories) {
    const allCategories = $(".product-category");
    allCategories.empty();
    const selectNone = `<option value="">please choose</option>`;
    allCategories.append(selectNone);
    for (let category of categories) {
      allCategories.append(createCategory(category));
      //allCategoriesArray.push(category);
    }
  }

  function createCategory(category) {
    const select = `<option value='${category.id}'>${category.title}</option>`;
    return select;
  }

  //SEARCH FUNCTION (dzt nur nach title möglich)
  $(document).on("click", "#showSearchProduct", function (event) {
    const searchId = $("#product-id").val();
    const search = $("#product-title-search").val();

    $.ajax({
      url: "http://localhost:8080/products/searchproduct/" + search,
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

  //ADD SEARCHED (FOUNDED) PRODUCTS TO LIST
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
      <form class="delete" data-title="delete" data-body="delete?">
        <button type="submit" class="btn btn-outline-danger" data-bs-toggle="modal"
          data-bs-target="#deleteModal">delete</button>
      </form>
    </td>
  </tr>`);
    return searchedProduct;
  }

  //LOAD PRODUCT TO EDIT FORM
  $(document).on("click", ".editProduct", function (event) {
    const id = event.target.value;
    console.log(id);

    $.ajax({
      url: "http://localhost:8080/products/" + id,
      type: "GET",
      cors: true,
      success: function (product) {
        editProducts(product);
      },
      error: function (error) {
        console.error(error);
      },
    });

    const addEditProduct = $("#addEditProduct");
    addEditProduct.empty();

    function editProducts(product) {
      const editProduct = $(`
    <p class="fs-4 fw-bold pt-2">Edit Product</p>
    <div class="row">
      <div class="col">
        <form>


          <div class="row mt-3 mb-3 ">
            <div class="col-md-2">
              <div class="form-group">
                <label for="product-id" class=" fs-5">Product Id</label>
                <input id="product-id" type="text" class="form-control " name="product-id" value="${product.id}" required>
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
              <select name="product-category" class="form-select fs-5 product-category" required>
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
                <textarea class="form-control" id="exampleFormControlTextarea1" rows="3" >${product.description}</textarea>
              </div>
            </div>
          </div>

          <div class="row mb-3">

            <div class="col-md-4">
              <div class="form-group">
                <label for="product-price" class="fs-5">Product Price</label>
                <input id="product-price" type="text" class="form-control " name="product-price" value="${product.price}" required>


              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label for="product-stock" class="   fs-5">Product Stock</label>
                <input id="product-stock" type="text" class="form-control " name="product-stock" value="${product.stock}" required>

              </div>
            </div>
            <div class="col-md-4 d-flex align-items-end">
              <div class="form-check mb-2 ">
                <input type="checkbox" class="form-check-input" name="status" id="status" checked>
                <label class="form-check-label fs-5" for="status">
                  active
                </label>
              </div>
            </div>

          </div>
          <div class="row mb-3">
            <div class="col-md-12">
            <label for="product-img" class="fs-5">Product Image Url</label>
            <input id="product-img" type="text" class="form-control " name="product-img" value="${product.img}" required>
            </div>
          </div>
          <button type="button" class="btn btn-warning text-white float-end mt-2 mb-2" id="saveEditProduct"> save</button>

        </form>
      </div>
    </div>`);

      addEditProduct.append(editProduct);
    }
  });
});
