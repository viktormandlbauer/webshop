package at.fhtw.webshop.controller.rest;

import at.fhtw.webshop.dto.ProductAddDto;
import at.fhtw.webshop.dto.product.ProductDto;
import at.fhtw.webshop.service.OrderService;
import at.fhtw.webshop.service.ProductService;
import at.fhtw.webshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

import org.springframework.data.domain.Pageable;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    public AdminRestController(ProductService productService, UserService userService, OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getUsersAsAdmin(Pageable pageable) {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", userService.getAllUsersAsListItem(pageable)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to retrieve users: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Object> getUserByIdAsAdmin(@PathVariable int id) {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", userService.getUserProfile(id)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to retrieve user: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/users/{id}/orders")
    public ResponseEntity<Object> getUserOrdersAsAdmin(@PathVariable int id, Pageable pageable) {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", orderService.getOrdersForUserAsListItem(id, pageable)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to retrieve user orders: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUserAsAdmin(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "User successfully deleted"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to delete user: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<Object> getOrdersAsAdmin(Pageable pageable) {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", orderService.getAllOrdersAsListItem(pageable)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to retrieve orders: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/products")
    public ResponseEntity<Object> getProductsAsAdmin(Pageable pageable) {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", productService.getAllProductsAsListItem(pageable)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to retrieve products: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getProductByIdAsAdmin(@PathVariable int id) {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", productService.getProductById(id)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to retrieve product: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProductAsAdmin(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Produkt erfolgreich gelöscht"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Fehler beim Löschen des Produkts: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProductAsAdmin(@PathVariable("id") int productId, @Valid @RequestBody ProductDto productDto) {
        try {
            productService.updateProduct(productId, productDto);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Successfully updated product with ID: " + productId
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to update product " + e.getMessage()
            ));
        }
    }

    // === Produkt hinzufügen bzw. Formular-Upload ===
    @PostMapping("/products/add")
    public ResponseEntity<?> addProduct(@Valid @ModelAttribute ProductAddDto dto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Formular enthält Fehler");
        }

        try {
            productService.saveProduct(dto);
            return ResponseEntity.ok("Produkt erfolgreich gespeichert");
        } catch (MultipartException e) {
            return ResponseEntity.badRequest().body("Fehler beim Dateiupload: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // <-- zeigt im Terminal, was genau falsch läuft!
            return ResponseEntity.internalServerError().body("Fehler beim Speichern des Produkts: " + e.getMessage());
        }
    }
}
