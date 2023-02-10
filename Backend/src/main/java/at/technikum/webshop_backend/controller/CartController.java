package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.model.Cart;
import at.technikum.webshop_backend.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public Cart create(@RequestBody Cart cart){
        return cartService.save(cart);
    }
}
