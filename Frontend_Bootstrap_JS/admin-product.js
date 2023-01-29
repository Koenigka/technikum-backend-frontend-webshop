$(document).ready(function(){
    $('#showNewProduct').click(function() {
      $('#createNewProduct').toggle();
    });

    
    $("#createProductButton").on("click", _e => {
      if($("#isActive").is(":checked")){
          const isActive = 1;
      }
      else {
          const isActive = 0;
      }   
  
      const product = {
  
        
  
          "title": $("#product-title").val(),
          "description": $("#product-description").val(),
          "price": $("#product-price").val(),
          "stock": $("#product-stock").val(),
          "img": $("#product-img").val(),
          "category_id": $("#product-category").val(),
          //Check if is checked --> value = 1 /0
          "isActive": "1" 
          }
  
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


    /* $('#showSearchProduct').click(function() {
      $('#SearchProduct').toggle();
    }); */
});