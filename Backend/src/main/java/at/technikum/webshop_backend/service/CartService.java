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


/**
 * Service class responsible for managing the user's shopping cart.
 * This class handles operations such as adding items to the cart.
 */
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

    /**
     * Adds a product item to the user's shopping cart.
     *
     * @param cartItemDto The CartItemDto containing the information of the item to be added to the cart.
     * @return The CartItem that has been added to the cart.
     * @throws EntityNotFoundException if the user or product specified in the CartItemDto is not found.
     */
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

    /**
     * Retrieves and returns a list of cart items belonging to a user.
     *
     * @param userId The ID of the user whose cart items are to be retrieved.
     * @return A list of CartItem objects representing the items in the user's shopping cart.
     * @throws EntityNotFoundException if the user specified by userId is not found.
     */
    public List<CartItem> viewCart(Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new EntityNotFoundException("User");
        }
        return cartItemRepository.findByUser(user);
    }

    /**
     * Updates a cart item in the user's shopping cart based on the provided CartItemDto.
     *
     * @param cartItemDto The CartItemDto containing the updated information for the cart item.
     * @return The updated CartItem object.
     * @throws EntityNotFoundException if the cart item specified by its ID in the CartItemDto is not found.
     */
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

    /**
     * Removes a cart item from the user's shopping cart based on the provided cart item ID.
     *
     * @param cartItemId The ID of the cart item to be removed from the cart.
     * @return A message indicating that the product has been removed from the cart.
     * @throws EntityNotFoundException if the cart item specified by cartItemId is not found.
     */
    public String removeFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItem == null) {
            throw new EntityNotFoundException("CartItem");
        }
        cartItemRepository.delete(cartItem);
        return "Product removed from the cart.";
    }

    /**
     * Clears the user's shopping cart by removing a list of cart items.
     *
     * @param cartItems The list of cart items to be removed from the cart.
     */
    public void clearCartItems(List<CartItem> cartItems) {
        cartItemRepository.deleteAll(cartItems);
    }

}
