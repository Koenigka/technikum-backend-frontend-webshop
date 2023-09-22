package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.dto.CartItemDto;
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

    private CartItem convertToCartItem(CartItemDto cartItemDto) {

        User user = userService.findById(cartItemDto.getUserId());
        Optional<Product> productOptional  = productService.findById(cartItemDto.getProductId());

        if (!productOptional.isPresent()) {
            throw new EntityNotFoundException("Product not found");
        }

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(productOptional.get());
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem.setCreationDate(LocalDateTime.now());
        return cartItem;
    }

    public CartItem addToCart(CartItemDto cartItemDto) {
        User user = userService.findById(cartItemDto.getUserId());
        Optional<Product> productOptional = productService.findById(cartItemDto.getProductId());

        if (user == null || !productOptional.isPresent()) {
            throw new EntityNotFoundException("User/Product");
        }

        CartItem cartItem = convertToCartItem(cartItemDto);


        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, productOptional);



        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItem.getQuantity());
            cartItemRepository.save(existingCartItem);
            return existingCartItem;
        } else {
            cartItemRepository.save(cartItem);
            return cartItem;
        }
    }

    public List<CartItem> viewCart(Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new EntityNotFoundException("User");
        }
        return cartItemRepository.findByUser(user);
    }

    public CartItem updateCart(CartItemDto cartItemDto) {
        CartItem existingCartItem = cartItemRepository.findById(cartItemDto.getId()).orElse(null);
        if (existingCartItem == null) {
            throw new EntityNotFoundException("CartItem");
        }

        existingCartItem.setCreationDate(LocalDateTime.now());

        existingCartItem.setQuantity(cartItemDto.getQuantity());
        cartItemRepository.save(existingCartItem);

        return existingCartItem;
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
