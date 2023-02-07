
    $('#showNewCategory').click(function() {
      $('#createNewCategory').toggle();
    });




    $("#createCategoryButton").on("click", _e => {
        console.log(e);
        /* if($("#isActive").is(":checked")){
            const isActive = 1;
        }
        else {
            const isActive = 0;
        }    */
    
        const category = {
    
          
    
            "title": $("#category-name").val(),
            "description": $("#category-description").val(),
            "imgUrl": $("#category-img-url").val(),
            //Check if is checked --> value = 1 /0
            "active": "1" 
            }
    
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


    

    
    

/*     
$('#showSearchCategory').click(function() {
    $('#SearchCategory').toggle();
  }); */
