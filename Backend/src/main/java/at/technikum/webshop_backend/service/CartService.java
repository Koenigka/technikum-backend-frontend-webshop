package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.model.CartItem;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private CartItemRepository cartItemRepository;
    private ProductService productService;
    private UserService userService;

    @Autowired
    public CartService(CartItemRepository cartItemRepository, UserService userService, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.productService = productService;
    }


    public String addToCart(CartItem cartItem) {
        User user = userService.findById(cartItem.getUser().getId());
        Optional<Product> product = productService.findById(cartItem.getProduct().getId());

        if (user == null || !product.isPresent()) {
            throw new EntityNotFoundException("User/Product");
        }

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        cartItem.setCreationDate(LocalDateTime.now());


        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItem.getQuantity());
            cartItemRepository.save(existingCartItem);
        } else {
            cartItemRepository.save(cartItem);
        }

        return "Product has been added to the cart.";
    }

    public List<CartItem> viewCart(Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new EntityNotFoundException("User");
        }
        return cartItemRepository.findByUser(user);
    }

    public String updateCart(CartItem cartItem) {
        CartItem existingCartItem = cartItemRepository.findById(cartItem.getId()).orElse(null);
        if (existingCartItem == null) {
            throw new EntityNotFoundException("CartItem");
        }

        existingCartItem.setCreationDate(LocalDateTime.now());

        existingCartItem.setQuantity(cartItem.getQuantity());
        cartItemRepository.save(existingCartItem);

        return "Cart updated.";
    }

    public String removeFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItem == null) {
            throw new EntityNotFoundException("CartItem");
        }
        cartItemRepository.delete(cartItem);
        return "Product removed from the cart.";
    }

    public void clearCartItems(List<CartItem> cartItems) {
        cartItemRepository.deleteAll(cartItems);
    }

}
