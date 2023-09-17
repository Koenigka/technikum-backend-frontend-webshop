package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.CartItem;
import at.technikum.webshop_backend.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Todo nur eingeloggte User dürfen einen cart anlegen
    @PostMapping
    public ResponseEntity<CartItem> create(@RequestBody CartItem cartItem) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.save(cartItem));
    }

    @PostMapping("/{userId}/add-product")
    public ResponseEntity<CartItem> addToCart(
            @PathVariable Long userId,
            @RequestBody ProductDto productDto,
            @RequestParam(name = "quantity", defaultValue = "1") int quantity
    ) {

        CartItem updatedCartItem = cartService.addToCart(userId, productDto.getId(), quantity);

        if (updatedCartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedCartItem);
    }

    /*
     *GET /api/cart/{userId}: Ruft den Warenkorb eines Benutzers ab.

POST /api/cart/{userId}/add: Fügt ein Produkt zum Warenkorb hinzu.

PUT /api/cart/{userId}/update: Aktualisiert die Menge eines Produkts im Warenkorb.

DELETE /api/cart/{userId}/remove: Entfernt ein Produkt aus dem Warenkorb.

GET /api/cart/{userId}/view: Zeigt den Inhalt des Warenkorbs an.
     *
     */
}
