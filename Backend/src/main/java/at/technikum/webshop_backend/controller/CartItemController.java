package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.dto.CartItemDto;
import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.CartItem;
import at.technikum.webshop_backend.service.CartService;
import at.technikum.webshop_backend.service.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartItemController {

    private CartService cartService;

    public CartItemController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemDto> addToCart(@RequestBody CartItemDto cartItemDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            try {
                CartItem cartItem = cartService.addToCart(cartItemDto);
                return new ResponseEntity<>(cartItem.convertToDto(), HttpStatus.OK);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/myCart")
    public ResponseEntity<List<CartItemDto>> viewCart(@RequestParam Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            try {
                List<CartItem> cartItems = cartService.viewCart(userId);
                List<CartItemDto> cartItemDtoList = new ArrayList<>();

                for (CartItem cartItem : cartItems) {
                    CartItemDto cartItemDto = cartItem.convertToDto();
                    cartItemDtoList.add(cartItemDto);
                }
                return new ResponseEntity<>(cartItemDtoList, HttpStatus.OK);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
    }

    @PutMapping("/update")
    public ResponseEntity<CartItemDto> updateCart(@RequestBody CartItemDto cartItemDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            try {
                CartItem updatedCartItem = cartService.updateCart(cartItemDto);
                return new ResponseEntity<>(updatedCartItem.convertToDto(), HttpStatus.OK);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }

    }


    //TODO Checkout Controller for proofing quantity


    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Long cartItemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            try {
                String result = cartService.removeFromCart(cartItemId);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}