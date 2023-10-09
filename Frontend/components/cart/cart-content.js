
// Event-Handler für das Offcanvas-Modal öffnen
$("#offcanvasNavbar").on("show.bs.offcanvas", function () {
    var userId = sessionStorage.getItem("userId");
    var accessToken = sessionStorage.getItem("accessToken");

    loadCartContent(userId, accessToken); 
    console.log("cartcontent goes here")
});

// Funktion zum Laden des Warenkorb-Inhalts
function loadCartContent(userId, accessToken) {

    //console.log("accesstoken" + accessToken);
    //console.log("userId " + userId);
   
    $.ajax({
        url: "http://localhost:8080/api/cart/myCart" + "?userId=" + userId, 
        type: "GET",
        beforeSend: function (xhr) {           
            xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
          },  
        success: function (data) {
            console.log("data " + data);
            $(".offcanvas-body").html(data); 
        },
        error: function (error) {
            console.error("Error loading cart content: " + error);
        }
    });
}


