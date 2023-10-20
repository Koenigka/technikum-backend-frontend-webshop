package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.dto.CartItemDto;
import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.CartItem;
import at.technikum.webshop_backend.security.UserPrincipal;
import at.technikum.webshop_backend.service.CartService;
import at.technikum.webshop_backend.service.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CartItemController} class handles HTTP requests related to shopping cart operations.
 * It exposes endpoints for adding items to the cart.
 *
 */
@RestController
@RequestMapping("/api/cart")
public class CartItemController {

    private CartService cartService;

    public CartItemController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Handles an HTTP POST request to add an item to the shopping cart.
     *
     * @param cartItemDto   The DTO (Data Transfer Object) representing the item to be added.
     * @param bindingResult The result of data binding and validation.
     * @return A ResponseEntity with an appropriate HTTP status code and response body.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            if (bindingResult.hasErrors()){
                return ResponseEntity.badRequest().body("Validation error");
            }
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

    /**
     * Handles an HTTP GET request to view the contents of the shopping cart for the authenticated user.
     *
     * @return A ResponseEntity containing a list of CartItemDto representing the items in the cart
     *         with an appropriate HTTP status code.
     */
    @GetMapping("/myCart")
    public ResponseEntity<List<CartItemDto>> viewCart() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Long userId = userPrincipal.getUserId();
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

    /**
     * Handles an HTTP PUT request to update an item in the shopping cart for the authenticated user.
     *
     * @param cartItemDto   The DTO (Data Transfer Object) representing the updated item.
     * @param bindingResult The result of data binding and validation.
     * @return A ResponseEntity with an appropriate HTTP status code and response body.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateCart(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            if (bindingResult.hasErrors()){
                return ResponseEntity.badRequest().body("Validation error");
            }
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

    /**
     * Handles an HTTP DELETE request to remove an item from the shopping cart for the authenticated user.
     *
     * @param cartItemId The ID of the cart item to be removed.
     * @return A ResponseEntity with an appropriate HTTP status code and response body.
     */
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long cartItemId) {
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
