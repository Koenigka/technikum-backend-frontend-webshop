$(document).ready(function(){

  //load new Product Form
    $('#showNewProduct').click(function() {
      $('#createNewProduct').toggle();
    });

     //Create new Product
     $("#createProductButton").on("click", _e => {
      /* if($("#isActive").is(":checked")){
          const isActive = 1;
      }
      else {
          const isActive = 0;
      }  */


      const categoryObject = [];
      const selectedCategory = $("#product-category").val();
      for (let item of allCategoriesArray) { 
        if (item.id == selectedCategory ){
          categoryObject.push(item);
        } 
      }

      const product = {
          "title": $("#product-title").val(),
          "description": $("#product-description").val(),
          "price": $("#product-price").val(),
          "stock": $("#product-stock").val(),
          "img": $("#product-img").val(),
          "category": categoryObject[0],
          //Check if is checked --> value = 1 /0
          "isActive": "1" 
          }
  //console.log(product);

          $.ajax({
              url: "http://localhost:8080/products",
              type: "POST",
              cors: true,
              contentType: "application/json",
              data: JSON.stringify(product),
              success: console.log,
              error: console.error
          });
          
      
  });

  //load Categories from Database & generate Dropdown values in new product form
  $.ajax({
    url: "http://localhost:8080/categories",
    type: "GET",
    cors: true,
    success: function(categories) { addCategories(categories) },
    error: function(error) { console.error(error) }
})

const allCategoriesArray = [];


function addCategories(categories){   
  const allCategories = $(".product-category");
  allCategories.empty();   
  const selectNone = (`<option value="">please choose</option>`)  
  allCategories.append(selectNone);  
  for (let category of categories) {
    allCategories.append(createCategory(category));
    allCategoriesArray.push(category);
  }
}

function createCategory(category) {
  
  const select = (`<option value='${(category.id)}'>${category.title}</option>`); 
  
return select;
}


    //Search Function (OPEN only Title possible)
    $(document).on("click", "#showSearchProduct", function(event){
      const searchId = $("#product-id").val();
      const search = $("#product-title-search").val();

      /* console.log(searchId);
      console.log(search); */
        
        $.ajax({
          url: "http://localhost:8080/products/searchproduct/" + search ,
          type: "GET",
          cors: true,         
          success: function(products) { addProducts(products) },
          error: function(error) { console.error(error) }
      });
      
    });

    //Add searched Products
    function addProducts(products){        
      const allSearchedProducts = $("#searchResult");
      allSearchedProducts.empty();        
      for (let product of products) {
        allSearchedProducts.append(createProduct(product));
      }
  }
  
  function createProduct(product) {

    const searchProduct = $(`<tr>
    <td scope="col">${product.id}</td>
    <td scope="col">${product.title}</td>
    <td scope="col">${product.category.title}</td>
    <td scope="col">${product.price}</td>
    <td scope="col">${product.stock}</td>
    <td scope="col"><button class="btn btn-outline-warning editProduct" id="${product.id}">edit</button></td>
    <td scope="col">
      <form class="delete" data-title="delete" data-body="delete?">
        <button type="submit" class="btn btn-outline-danger" data-bs-toggle="modal"
          data-bs-target="#deleteModal">delete</button>
      </form>
    </td>
  </tr>`) 
  return searchProduct;
  }
 
// to do --> Product details inf form ausgeben !!!!
  $(document).on("click", ".editProduct", function(event){

    const addEditProduct = $("#addEditProduct");
   
    const editProduct = $(`
    <p class="fs-4 fw-bold pt-2">Edit Product</p>
    <div class="row">
      <div class="col">
        <form method="POST" action="">


          <div class="row mt-3 mb-3 ">
            <div class="col-md-2">
              <div class="form-group">
                <label for="product-id" class=" fs-5">Product Id</label>
                <input id="product-id" type="text" class="form-control " name="product-id" value="" required>
              </div>
            </div>
            <div class="col-md-5">
              <div class="form-group">
                <label for="product-name" class=" fs-5">Product Name</label>
                <input id="product-name" type="text" class="form-control " name="product-name" value="" required>
              </div>
            </div>
            <div class="col-md-5">
              <div class="form-group">
                <label for="product-category" class="fs-5">Product Category</label>

                <select name="product-category" class="form-select fs-5" id="product-category" required>
                  <option value=""></option>
                  <option value="1">category1</option>
                  <option value="2">category2</option>
                  <option value="3">category3</option>
                </select>
              </div>
            </div>


          </div>


          <div class="row mb-3">
            <div class="col-md-12">
              <div class="form-group">
                <label for="product-description" class=" fs-5">Product Description</label>
                <textarea class="form-control" id="exampleFormControlTextarea1" rows="3"></textarea>
              </div>
            </div>
          </div>

          <div class="row mb-3">

            <div class="col-md-4">
              <div class="form-group">
                <label for="product-price" class="fs-5">Product Price</label>
                <input id="product-price" type="text" class="form-control " name="product-price" value="" required>


              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label for="product-stock" class="   fs-5">Product Stock</label>
                <input id="product-stock" type="text" class="form-control " name="product-stock" value="" required>

              </div>
            </div>
            <div class="col-md-4 d-flex align-items-end">
              <div class="form-check mb-2 ">
                <input type="checkbox" class="form-check-input" name="status" id="status">
                <label class="form-check-label fs-5" for="status">
                  active
                </label>
              </div>
            </div>

          </div>
          <div class="row mb-3">
            <div class="col-md-12">
              <label for="product-img" class="fs-5">Product Image</label>
              <div class="input-group mb-2">
                <input class="form-control" type="file" id="file" name="file" required>
              </div>
            </div>
          </div>
          <button type="submit" class="btn btn-warning text-white float-end mt-2 mb-2"> save</button>

        </form>
      </div>
    </div>
  `);
  addEditProduct.append(editProduct);
    
  });
 
});