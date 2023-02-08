$(document).ready(function () {

    //TOGGLE FOR CREATE CATEGORY FORM
    $('#showNewCategory').click(function() {
      $('#createNewCategory').toggle();
    });

    //CREATE NEW CATEGORY
    $("#createCategoryButton").on("click", (_e) => {
         //Validation open
    //Checkbox true false open    
        const category = {
            title: $("#category-title").val(),
            description: $("#category-description").val(),
            imgUrl: $("#category-img-url").val(),
            //Check if is checked --> value = true/false
            active: "true"
            };
    
            $.ajax({
                url: "http://localhost:8080/categories",
                type: "POST",
                cors: true,
                contentType: "application/json",
                data: JSON.stringify(category),
                success: console.log,
                error: console.error
            });
    });
    //SEARCH FUNCTION (dzt nur nach Title ohne aktiv!)
    $(document).on("click", "#showSearchCategory", function (event) {
        const searchId = $("#category-id").val();
        const search = $("#category-name-search").val();
        
    
        $.ajax({
          url: "http://localhost:8080/categories/searchCategoryTitle/" + search,
          type: "GET",
          cors: true,
          success: function (categories) {
            addCategories(categories);
          },
          error: function (error) {
            console.error(error);
          },
        });
      });


      //ADD SEARCHED CATEGORIES FROM DATABASE TO LIST
  function addCategories(categories) {


    const allSearchedCategories = $("#searchResult");
    allSearchedCategories.empty();  

    for (let category of categories) {
        allSearchedCategories.append(createCategory(category));
    }
  }

  function createCategory(category) {

    const searchedCategory = $(`<tr>
    <td scope="col">${category.id}</td>
    <td scope="col">${category.title}</td>    
    <td scope="col"><button class="btn btn-outline-warning editCategory" value="${category.id}">edit</button></td>
    <td scope="col">
      <form action="#" method="post" class="delete" data-title="delete" data-body="delete?">
        <button type="submit" class="btn btn-outline-danger" data-bs-toggle="modal"
          data-bs-target="#deleteModal">delete</button>
      </form>
    </td>
  </tr>`)
  return searchedCategory;
    
   
  }

  //LOAD CATEGORY TO EDIT FORM
  $(document).on("click", ".editCategory", function (event) {
    const id = event.target.value;
    //console.log(id);
    $.ajax({
      url: "http://localhost:8080/categories/" + id,
      type: "GET",
      cors: true,
      success: function (category) {
        editCategories(category);
      },
      error: function (error) {
        console.error(error);
      },
    });

    const addEditCategory = $("#addEditCategory");
    addEditCategory.empty();

    function editCategories(category) {
      const editCategory = $(`  <div class="container rounded mt-5 border border-warning bg-light shadow-lg">
      <p class="fs-4 fw-bold pt-2">Edit Category</p>
      <div class="row">
        <div class="col">
          <form> 
            <div class="row mt-3 mb-3 ">
              <div class="col-md-3">
                <div class="form-group">
                  <label for="category-id" class=" fs-5">Category Id</label>
                  <input id="category-id-edit" type="text" class="form-control " name="category-id-edit" value="${category.id}" required>
                </div>
              </div>
              <div class="col-md-9">
                <div class="form-group">
                  <label for="category-name" class=" fs-5">Category Name</label>
                  <input id="category-name" type="text" class="form-control " name="category-name" value="${category.title}" required>
                </div>
              </div>
            </div>
  
  
            <div class="row mb-3">
              <div class="col-md-12">
                <div class="form-group">
                  <label for="product-description" class=" fs-5">Category Description</label>
                  <textarea class="form-control" id="category-description-edit" rows="3">${category.description}</textarea>
                </div>
              </div>
            </div>
  
  
            <div class="row mb-3">
              <div class="col-md-12">
                <label for="category-img" class="fs-5">Category Image Url</label>
                <div class="input-group mb-2">
                  <input class="form-control" type="text" id="category-img" name="category-img" value="${category.imgUrl}"required>
                </div>
              </div>
            </div>
            <button type="button" class="btn btn-warning text-white float-end mt-2 mb-2" id="saveEditCategory"> save</button>
  
          </form>
        </div>
      </div>
    </div>`);

    addEditCategory.append(editCategory);
    }



});

//EDIT CATEGORY

$(document).on("click", "#saveEditCategory", function (event) {
  
    const id = $("#category-id-edit").val();

    //console.log(id);    
    const category = {
      title: $("#category-name").val(),
      description: $("#category-description-edit").val(),      
      imgUrl: $("#category-img").val(),      
      active: "true"
    };

    //console.log(category);
    $.ajax({
      url: "http://localhost:8080/categories/" + id,
      type: "PUT",
      cors: true,
      contentType: "application/json",
      data: JSON.stringify(category),
      success: console.log,
      error: console.error,
    });
  });
  
  //DELETE CATEGORY

});
    

    
    

