package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.dto.CustomerOrderDto;
import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.enums.Status;
import at.technikum.webshop_backend.model.*;
import at.technikum.webshop_backend.repository.OrderItemRepository;
import at.technikum.webshop_backend.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing customer orders, order items, and order-related operations.
 */
@Service
public class OrderService {

    private OrderRepository orderRepository;
    private CartService cartService;
    private UserService userService;

    private ProductService productService;

    private OrderItemRepository orderItemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public OrderService(OrderRepository orderRepository, CartService cartService, UserService userService, OrderItemRepository orderItemRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.userService = userService;
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
    }

    /**
     * Creates a new customer order from the user's cart and deducts product quantities.
     *
     * @param userId The ID of the user for whom the order is created.
     * @return The created CustomerOrder.
     * @throws EntityNotFoundException if the user is not found or the cart is empty.
     */
    public CustomerOrder createOrderFromCart(Long userId) {

        User user = userService.findById(userId);

        if (user == null) {
            throw new EntityNotFoundException("User");
        }

        List<CartItem> cartItems = cartService.viewCart(userId);

        System.out.println(cartItems.size());

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Empty cart");
        }

        CustomerOrder newCustomerOrder = new CustomerOrder();
        newCustomerOrder.setUser(user);
        newCustomerOrder.setOrderDate(LocalDateTime.now());
        newCustomerOrder.setStatus(Status.IN_PROGRESS);

        newCustomerOrder = orderRepository.save(newCustomerOrder);


        for (CartItem cartItem : cartItems) {
            deductProductQuantity(cartItem.getProduct().getId(), cartItem.getQuantity());
            OrderItem orderItem = new OrderItem();
            orderItem.setCustomerOrder(newCustomerOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItemRepository.save(orderItem);
            newCustomerOrder.getOrderItems().add(orderItem);

        }

        newCustomerOrder = orderRepository.save(newCustomerOrder);

        cartService.clearCartItems(cartItems);

        return newCustomerOrder;
    }


    /**
     * Checks product quantities in the user's cart against available quantities.
     *
     * @param userId The ID of the user whose cart is checked.
     * @return A map containing products with insufficient quantities and their available quantities.
     */
    public Map<String, Integer> checkProductQuantities(Long userId) {

        List<CartItem> cartItems = cartService.viewCart(userId);

        Map<String, Integer> insufficientQuantities = new HashMap<>();

        for (CartItem cartItem : cartItems) {
            Long productId = cartItem.getProduct().getId();
            int requestedQuantity = cartItem.getQuantity();

            int availableQuantity = getAvailableProductQuantity(productId);

            if (availableQuantity < requestedQuantity) {
                insufficientQuantities.put(cartItem.getProduct().getTitle(),  availableQuantity);
            }
        }

        return insufficientQuantities;
    }

    /**
     * Retrieves the available quantity of a product by its ID.
     *
     * @param productId The ID of the product to check.
     * @return The available quantity of the product.
     * @throws EntityNotFoundException if the product is not found.
     */
    public int getAvailableProductQuantity(Long productId) {
        Optional<Product> optionalProduct = productService.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            return product.getStock();
        } else {
            throw new EntityNotFoundException("Product not found with ID: " + productId);
        }
    }


    /**
     * Deducts product quantity from stock.
     *
     * @param productId       The ID of the product to deduct from.
     * @param quantityToDeduct The quantity to deduct.
     * @throws EntityNotFoundException if the product is not found or IllegalArgumentException if the quantity is insufficient.
     */
    public void deductProductQuantity(Long productId, int quantityToDeduct) {
        Optional<Product> optionalProduct = productService.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            if (product.getStock() >= quantityToDeduct) {
                product.setStock(product.getStock() - quantityToDeduct);

                productService.save(product);
            } else {
                throw new IllegalArgumentException("Insufficient product quantity for product with ID: " + productId);
            }
        } else {
            throw new EntityNotFoundException("Product not found with ID: " + productId);
        }
    }

    /**
     * Retrieves a list of user orders by user ID.
     *
     * @param userId The ID of the user.
     * @return A list of CustomerOrder objects belonging to the user.
     */
    public List<CustomerOrder> getUserOrdersByUserId(Long userId) {
        User user = userService.findById(userId);

        if (user != null) {
            return orderRepository.findByUser(user);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves a user order by its order ID.
     *
     * @param orderId The ID of the order to retrieve.
     * @return The CustomerOrder object with the specified ID or null if not found.
     */
    public CustomerOrder getUserOrderByOrderId(Long orderId) {
        Optional<CustomerOrder> order = orderRepository.findById(orderId);
        return order.orElse(null);
    }


    /**
     * Retrieves a list of customer orders based on specified filters.
     *
     * @param filters A map of filter criteria for querying orders.
     * @return A list of CustomerOrder objects matching the filter criteria.
     */
    public List<CustomerOrder> findOrdersByFilters(Map<String, String> filters) {
        Specification<CustomerOrder> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            String userEmail = filters.get("email");
            if (userEmail != null && !userEmail.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.join("user").get("email"), userEmail + "%"));
            }


            String orderIdStr = filters.get("orderId");
            if (orderIdStr != null) {
                Long orderId = Long.parseLong(orderIdStr);
                predicates.add(criteriaBuilder.equal(root.get("id"), orderId));
            }

            String orderDateStr = filters.get("orderDate");
            if (orderDateStr != null) {
                LocalDate orderDate = LocalDate.parse(orderDateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                predicates.add(criteriaBuilder.equal(root.get("orderDate").as(LocalDate.class), orderDate));
            }

            String orderStatusStr = filters.get("orderStatus");
            if (orderStatusStr != null) {
                Status orderStatus = Status.valueOf(orderStatusStr);
                predicates.add(criteriaBuilder.equal(root.get("status"), orderStatus));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return orderRepository.findAll(spec);
    }




   /*public List<CustomerOrder> findOrdersByFilters(Map<String, String> filters) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CustomerOrder> query = criteriaBuilder.createQuery(CustomerOrder.class);
        Root<CustomerOrder> root = query.from(CustomerOrder.class);

        Predicate predicate = criteriaBuilder.conjunction();

        String userEmail = filters.get("userEmail");
        if (userEmail != null && !userEmail.isEmpty()) {
            Join<CustomerOrder, User> userJoin = root.join("user");
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(userJoin.get("email"), userEmail));
        }

        String userName = filters.get("userName");
        if (userName != null && !userName.isEmpty()) {
            Join<CustomerOrder, User> userJoin = root.join("user");
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(userJoin.get("username"), userName));
        }

        String orderIdStr = filters.get("orderId");
        if (orderIdStr != null && !orderIdStr.isEmpty()) {
            Long orderId = Long.parseLong(orderIdStr);
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("id"), orderId));
        }

        String orderDateStr = filters.get("orderDate");
        if (orderDateStr != null && !orderDateStr.isEmpty()) {
            LocalDate orderDate = LocalDate.parse(orderDateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("orderDate").as(LocalDate.class), orderDate));
        }

        String orderStatusStr = filters.get("orderStatus");
        if (orderStatusStr != null && !orderStatusStr.isEmpty()) {
            try {
                Status orderStatus = Status.valueOf(orderStatusStr);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), orderStatus));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Illegal Status: " + orderStatusStr);
            }
        }

        query.where(predicate);

        return entityManager.createQuery(query).getResultList();
    }*/


    /**
     * Converts a list of CustomerOrder objects to a list of CustomerOrderDto objects.
     *
     * @param customerOrder The list of CustomerOrder objects to convert.
     * @return A list of CustomerOrderDto objects.
     */
    public List<CustomerOrderDto> convertToOrderDtoList(List<CustomerOrder> customerOrder) {
        return customerOrder.stream()
                .map(CustomerOrder::convertToDto)
                .collect(Collectors.toList());
    }

   /* public CustomerOrder updateOrder(CustomerOrderDto orderDto) {
        Long orderId = orderDto.getId();
        Status newStatus = orderDto.getStatus();
        Optional<CustomerOrder> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            CustomerOrder order = optionalOrder.get();
            order.setStatus(newStatus);

            order = orderRepository.save(order);
            return order;
        } else {
            throw new EntityNotFoundException("Order not found with ID: " + orderId);
        }
    }*/


    /**
     * Updates the status of a customer order and, if canceled, increases product quantities.
     *
     * @param orderDto The CustomerOrderDto containing the updated status.
     * @return The updated CustomerOrder.
     * @throws EntityNotFoundException if the order is not found.
     */
    public CustomerOrder updateOrder(CustomerOrderDto orderDto) {
        Long orderId = orderDto.getId();
        Status newStatus = orderDto.getStatus();
        Optional<CustomerOrder> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            CustomerOrder customerOrder = optionalOrder.get();

            if (newStatus == Status.CANCELED) {
                Set<OrderItem> orderItems = customerOrder.getOrderItems();
                for (OrderItem orderItem : orderItems) {
                    Product product = orderItem.getProduct();
                    int canceledQuantity = orderItem.getQuantity();
                    product.increaseStock(canceledQuantity);
                }
            }

            customerOrder.setStatus(newStatus);

            customerOrder = orderRepository.save(customerOrder);
            return customerOrder;
        } else {
            throw new EntityNotFoundException("Order not found with ID: " + orderId);
        }
    }

}
