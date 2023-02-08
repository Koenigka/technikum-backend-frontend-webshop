$(document).ready(function () {
    $('#showNewCategory').click(function() {
      $('#createNewCategory').toggle();
    });




    $("#createCategoryButton").on("click", (_e) => {
        
        /* if($("#isActive").is(":checked")){
            const isActive = 1;
        }
        else {
            const isActive = 0;
        }    */
    
        const category = {
    
            
            title: $("#category-title").val(),
            description: $("#category-description").val(),
            imgUrl: $("#category-img-url").val(),
            //Check if is checked --> value = 1 /0
            active: "true"
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

});
    

    
    

/*     
$('#showSearchCategory').click(function() {
    $('#SearchCategory').toggle();
  }); */
