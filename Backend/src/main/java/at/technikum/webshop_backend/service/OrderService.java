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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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


    public Map<Long, Integer> checkProductQuantities(Long userId) {

        List<CartItem> cartItems = cartService.viewCart(userId);

        Map<Long, Integer> insufficientQuantities = new HashMap<>();

        for (CartItem cartItem : cartItems) {
            Long productId = cartItem.getProduct().getId();
            int requestedQuantity = cartItem.getQuantity();

            int availableQuantity = getAvailableProductQuantity(productId);

            if (availableQuantity < requestedQuantity) {
                insufficientQuantities.put(cartItem.getProduct().getId(),  availableQuantity);
            }
        }

        return insufficientQuantities;
    }


    public int getAvailableProductQuantity(Long productId) {
        Optional<Product> optionalProduct = productService.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            return product.getStock();
        } else {
            throw new EntityNotFoundException("Product not found with ID: " + productId);
        }
    }



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


    public List<CustomerOrder> getUserOrdersByUserId(Long userId) {
        User user = userService.findById(userId);

        if (user != null) {
            return orderRepository.findByUser(user);
        } else {
            return Collections.emptyList();
        }
    }

    public CustomerOrder getUserOrderByOrderId(Long orderId) {
        Optional<CustomerOrder> order = orderRepository.findById(orderId);
        return order.orElse(null);
    }


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
