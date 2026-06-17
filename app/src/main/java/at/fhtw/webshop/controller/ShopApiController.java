package at.fhtw.webshop.controller;

import at.fhtw.webshop.dto.RegistrationDto;
import at.fhtw.webshop.dto.ShopDtos.*;
import at.fhtw.webshop.model.*;
import at.fhtw.webshop.repository.*;
import at.fhtw.webshop.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/shop")
public class ShopApiController {
    private static final String CART_SESSION_KEY = "cart";
    private static final String REMEMBER_COOKIE = "rememberUser";
    private static final String VOUCHER_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ShopOrderRepository orderRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final VoucherRepository voucherRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public ShopApiController(UserRepository userRepository,
                             ProductRepository productRepository,
                             CategoryRepository categoryRepository,
                             ShopOrderRepository orderRepository,
                             PaymentMethodRepository paymentMethodRepository,
                             VoucherRepository voucherRepository,
                             UserService userService,
                             AuthenticationManager authenticationManager,
                             PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.voucherRepository = voucherRepository;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/session")
    public Map<String, Object> session(HttpServletRequest request) {
        User user = currentUser(request).orElse(null);
        return Map.of("user", safeUser(user), "cart", cartResponse(request.getSession()));
    }

    @PostMapping("/register")
    public Map<String, Object> register(@Valid @RequestBody RegistrationDto dto) {
        if (!Objects.equals(dto.getPassword(), dto.getConfirmPassword())) {
            throw new ResponseStatusException(BAD_REQUEST, "Passwoerter stimmen nicht ueberein");
        }
        if (userService.emailExists(dto.getEmail()) || userService.usernameExists(dto.getUsername())) {
            throw new ResponseStatusException(CONFLICT, "E-Mail oder Benutzername existiert bereits");
        }
        userService.registerUser(dto);
        return Map.of("message", "Registrierung erfolgreich");
    }

    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody LoginRequest request,
                                     HttpServletRequest servletRequest,
                                     HttpServletResponse response) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.identifier(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        servletRequest.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        if (request.rememberMe()) {
            Cookie cookie = new Cookie(REMEMBER_COOKIE, auth.getName());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            response.addCookie(cookie);
        }
        return Map.of("user", safeUser(userService.findByUsernameOrEmail(auth.getName())));
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        Cookie cookie = new Cookie(REMEMBER_COOKIE, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return Map.of("message", "Logout erfolgreich");
    }

    @GetMapping("/categories")
    public List<Category> categories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/products")
    public List<Product> products(@RequestParam(required = false) Integer categoryId,
                                  @RequestParam(required = false) String q) {
        if (q != null && !q.isBlank()) {
            return productRepository.findTop20ByNameContainingIgnoreCase(q);
        }
        if (categoryId != null) {
            return productRepository.findAll().stream()
                    .filter(product -> product.getCategory().getId().equals(categoryId))
                    .toList();
        }
        return productRepository.findAll();
    }

    @GetMapping("/cart")
    public Map<String, Object> cart(HttpSession session) {
        return cartResponse(session);
    }

    @PostMapping("/cart/items")
    public Map<String, Object> addToCart(@Valid @RequestBody CartChangeRequest request, HttpSession session) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Produkt nicht gefunden"));
        Map<Integer, Integer> cart = cartMap(session);
        int quantity = Math.max(1, request.quantity());
        cart.merge(product.getId(), quantity, Integer::sum);
        return cartResponse(session);
    }

    @PatchMapping("/cart/items/{productId}")
    public Map<String, Object> updateCart(@PathVariable Integer productId,
                                          @Valid @RequestBody CartChangeRequest request,
                                          HttpSession session) {
        Map<Integer, Integer> cart = cartMap(session);
        if (request.quantity() <= 0) {
            cart.remove(productId);
        } else {
            cart.put(productId, request.quantity());
        }
        return cartResponse(session);
    }

    @DeleteMapping("/cart/items/{productId}")
    public Map<String, Object> removeFromCart(@PathVariable Integer productId, HttpSession session) {
        cartMap(session).remove(productId);
        return cartResponse(session);
    }

    @PostMapping("/checkout")
    @Transactional
    public ShopOrder checkout(@Valid @RequestBody CheckoutRequest request, HttpServletRequest servletRequest) {
        User user = requireCustomer(servletRequest);
        Map<Integer, Integer> cart = cartMap(servletRequest.getSession());
        if (cart.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Warenkorb ist leer");
        }
        PaymentMethod paymentMethod = null;
        if (request.paymentMethodId() != null) {
            paymentMethod = paymentMethodRepository.findById(request.paymentMethodId())
                    .filter(pm -> pm.getUser().getId().equals(user.getId()))
                    .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Zahlungsart nicht gefunden"));
        }
        if (paymentMethod == null && (request.voucherCode() == null || request.voucherCode().isBlank())) {
            throw new ResponseStatusException(BAD_REQUEST, "Zahlungsart oder Gutschein erforderlich");
        }

        ShopOrder order = new ShopOrder();
        order.setUser(user);
        order.setPaymentLabel(paymentMethod == null ? "Gutschein" : paymentMethod.getLabel());

        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Produkt nicht gefunden"));
            ShopOrderItem item = new ShopOrderItem();
            item.setProduct(product);
            item.setQuantity(entry.getValue());
            item.setUnitPrice(product.getPrice());
            order.addItem(item);
            total = total.add(item.getLineTotal());
        }

        BigDecimal discount = redeemVoucher(request.voucherCode(), total);
        order.setVoucherCode(request.voucherCode());
        order.setTotal(total);
        order.setVoucherDiscount(discount);
        order.setFinalTotal(total.subtract(discount).max(BigDecimal.ZERO));
        ShopOrder saved = orderRepository.save(order);
        cart.clear();
        return saved;
    }

    @GetMapping("/account")
    public Map<String, Object> account(HttpServletRequest request) {
        User user = requireCustomer(request);
        return Map.of(
                "user", safeUser(user),
                "paymentMethods", maskedPayments(user),
                "orders", orderRepository.findByUserOrderByOrderDateAsc(user)
        );
    }

    @PutMapping("/account")
    public Map<String, Object> updateAccount(@Valid @RequestBody AccountUpdateRequest request,
                                             HttpServletRequest servletRequest) {
        User user = requireCustomer(servletRequest);
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(FORBIDDEN, "Passwort ist falsch");
        }
        user.setSalutation(request.salutation());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setAddress(request.address());
        user.setZip(request.zip());
        user.setCity(request.city());
        userRepository.save(user);
        return Map.of("user", safeUser(user));
    }

    @PostMapping("/account/payments")
    public List<Map<String, Object>> addPayment(@Valid @RequestBody PaymentMethodRequest request,
                                                HttpServletRequest servletRequest) {
        User user = requireCustomer(servletRequest);
        PaymentMethod method = new PaymentMethod();
        method.setUser(user);
        method.setLabel(request.label());
        method.setDetails(request.details());
        paymentMethodRepository.save(method);
        return maskedPayments(user);
    }

    @GetMapping(value = "/orders/{orderId}/invoice", produces = MediaType.TEXT_HTML_VALUE)
    public String invoice(@PathVariable Integer orderId, HttpServletRequest request) {
        User user = currentUser(request).orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED));
        ShopOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        if (!isAdmin(user) && !order.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(FORBIDDEN);
        }
        if (order.getInvoiceNumber() == null) {
            order.setInvoiceNumber("RE-" + order.getId() + "-" + System.currentTimeMillis());
            orderRepository.save(order);
        }
        return renderInvoice(order);
    }

    @GetMapping("/admin/customers")
    public List<Map<String, Object>> customers(HttpServletRequest request) {
        requireAdmin(request);
        return userRepository.findAll().stream().map(this::safeUser).toList();
    }

    @PatchMapping("/admin/customers/{id}/active")
    public Map<String, Object> setCustomerActive(@PathVariable Integer id,
                                                 @RequestParam boolean active,
                                                 HttpServletRequest request) {
        requireAdmin(request);
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        user.setActive(active);
        userRepository.save(user);
        return safeUser(user);
    }

    @GetMapping("/admin/customers/{id}/orders")
    public List<ShopOrder> customerOrders(@PathVariable Integer id, HttpServletRequest request) {
        requireAdmin(request);
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        return orderRepository.findByUserOrderByOrderDateAsc(user);
    }

    @DeleteMapping("/admin/orders/{orderId}/items/{itemId}")
    @Transactional
    public ShopOrder removeOrderItem(@PathVariable Integer orderId,
                                     @PathVariable Integer itemId,
                                     HttpServletRequest request) {
        requireAdmin(request);
        ShopOrder order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        order.getItems().removeIf(item -> item.getId().equals(itemId));
        BigDecimal total = order.getItems().stream()
                .map(ShopOrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
        order.setFinalTotal(total.subtract(order.getVoucherDiscount()).max(BigDecimal.ZERO));
        return orderRepository.save(order);
    }

    @PostMapping("/admin/vouchers")
    public Voucher createVoucher(@Valid @RequestBody VoucherCreateRequest request, HttpServletRequest servletRequest) {
        requireAdmin(servletRequest);
        Voucher voucher = new Voucher();
        voucher.setCode(uniqueVoucherCode());
        voucher.setValue(request.value());
        voucher.setRemainingValue(request.value());
        voucher.setExpiresAt(request.expiresAt());
        return voucherRepository.save(voucher);
    }

    @GetMapping("/admin/vouchers")
    public List<Voucher> vouchers(HttpServletRequest request) {
        requireAdmin(request);
        return voucherRepository.findAll();
    }

    @PutMapping("/admin/products/{id}")
    public Product updateProduct(@PathVariable Integer id,
                                 @Valid @RequestBody ProductUpdateRequest request,
                                 HttpServletRequest servletRequest) {
        requireAdmin(servletRequest);
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Kategorie nicht gefunden"));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setAvgRating(request.avgRating());
        product.setStock(request.stock());
        product.setCategory(category);
        if (request.imageURL() != null && !request.imageURL().isBlank()) {
            product.setImageURL(request.imageURL());
        }
        return productRepository.save(product);
    }

    @DeleteMapping("/admin/products/{id}")
    public Map<String, String> deleteProduct(@PathVariable Integer id, HttpServletRequest request) {
        requireAdmin(request);
        productRepository.deleteById(id);
        return Map.of("message", "Produkt geloescht");
    }

    private BigDecimal redeemVoucher(String code, BigDecimal total) {
        if (code == null || code.isBlank()) {
            return BigDecimal.ZERO;
        }
        Voucher voucher = voucherRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Gutschein nicht gefunden"));
        if (voucher.isExpired() || voucher.isRedeemed()) {
            throw new ResponseStatusException(BAD_REQUEST, "Gutschein ist abgelaufen oder verbraucht");
        }
        BigDecimal discount = voucher.getRemainingValue().min(total).setScale(2, RoundingMode.HALF_UP);
        voucher.setRemainingValue(voucher.getRemainingValue().subtract(discount));
        voucherRepository.save(voucher);
        return discount;
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> cartMap(HttpSession session) {
        Object existing = session.getAttribute(CART_SESSION_KEY);
        if (existing instanceof Map<?, ?> map) {
            return (Map<Integer, Integer>) map;
        }
        Map<Integer, Integer> cart = new LinkedHashMap<>();
        session.setAttribute(CART_SESSION_KEY, cart);
        return cart;
    }

    private Map<String, Object> cartResponse(HttpSession session) {
        List<Map<String, Object>> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;
        for (Map.Entry<Integer, Integer> entry : cartMap(session).entrySet()) {
            Product product = productRepository.findById(entry.getKey()).orElse(null);
            if (product == null) {
                continue;
            }
            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
            items.add(Map.of("product", product, "quantity", entry.getValue(), "lineTotal", lineTotal));
            total = total.add(lineTotal);
            count += entry.getValue();
        }
        return Map.of("items", items, "total", total, "count", count);
    }

    private Optional<User> currentUser(HttpServletRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            return Optional.ofNullable(userRepository.findByUsername(authentication.getName()));
        }
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> REMEMBER_COOKIE.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .map(userRepository::findByUsername)
                    .filter(Objects::nonNull)
                    .filter(User::isActive)
                    .findFirst();
        }
        return Optional.empty();
    }

    private User requireCustomer(HttpServletRequest request) {
        User user = currentUser(request).orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Login erforderlich"));
        if (!user.isActive()) {
            throw new ResponseStatusException(FORBIDDEN, "Account deaktiviert");
        }
        return user;
    }

    private User requireAdmin(HttpServletRequest request) {
        User user = requireCustomer(request);
        if (!isAdmin(user)) {
            throw new ResponseStatusException(FORBIDDEN, "Adminrechte erforderlich");
        }
        return user;
    }

    private boolean isAdmin(User user) {
        return user != null && "Admin".equals(user.getRole());
    }

    private Map<String, Object> safeUser(User user) {
        if (user == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", user.getId());
        result.put("salutation", user.getSalutation());
        result.put("firstName", user.getFirstName());
        result.put("lastName", user.getLastName());
        result.put("email", user.getEmail());
        result.put("username", user.getUsername());
        result.put("address", Objects.toString(user.getAddress(), ""));
        result.put("zip", Objects.toString(user.getZip(), ""));
        result.put("city", Objects.toString(user.getCity(), ""));
        result.put("role", user.getRole());
        result.put("active", user.isActive());
        result.put("paymentInfoMasked", mask(user.getPaymentInfo()));
        return result;
    }

    private List<Map<String, Object>> maskedPayments(User user) {
        return paymentMethodRepository.findByUser(user).stream()
                .map(method -> Map.<String, Object>of(
                        "id", method.getId(),
                        "label", method.getLabel(),
                        "maskedDetails", method.getMaskedDetails()))
                .toList();
    }

    private String mask(String value) {
        if (value == null || value.length() < 4) {
            return "****";
        }
        return "**** " + value.substring(value.length() - 4);
    }

    private String uniqueVoucherCode() {
        String code;
        do {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                builder.append(VOUCHER_CHARS.charAt(secureRandom.nextInt(VOUCHER_CHARS.length())));
            }
            code = builder.toString();
        } while (voucherRepository.findByCodeIgnoreCase(code).isPresent());
        return code;
    }

    private String renderInvoice(ShopOrder order) {
        String rows = order.getItems().stream()
                .map(item -> "<tr><td>" + item.getProduct().getName() + "</td><td>" + item.getQuantity()
                        + "</td><td>" + item.getUnitPrice() + " EUR</td><td>" + item.getLineTotal() + " EUR</td></tr>")
                .reduce("", String::concat);
        User user = order.getUser();
        return """
                <!doctype html><html lang="de"><head><meta charset="utf-8"><title>Rechnung</title>
                <style>body{font-family:Arial;margin:40px;color:#2f2525}table{width:100%%;border-collapse:collapse}td,th{border-bottom:1px solid #ddd;padding:10px;text-align:left}.total{text-align:right;font-size:1.2rem}</style>
                </head><body onload="window.print()"><h1>Rechnung %s</h1>
                <p><strong>Datum:</strong> %s</p><p>%s %s<br>%s<br>%s %s</p>
                <table><thead><tr><th>Produkt</th><th>Anzahl</th><th>Einzelpreis</th><th>Summe</th></tr></thead><tbody>%s</tbody></table>
                <p class="total">Gutschein: -%s EUR<br><strong>Gesamt: %s EUR</strong></p></body></html>
                """.formatted(order.getInvoiceNumber(),
                order.getOrderDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                user.getFirstName(), user.getLastName(), user.getAddress(), user.getZip(), user.getCity(),
                rows, order.getVoucherDiscount(), order.getFinalTotal());
    }
}
