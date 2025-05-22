package at.fhtw.webshop.controller;

import at.fhtw.webshop.dto.ProductAddDto;
import at.fhtw.webshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    private final ProductService productService;

    public AdminRestController(ProductService productService) {
        this.productService = productService;
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
            e.printStackTrace(); // <-- Zeigt im Terminal, was genau falsch läuft!
            return ResponseEntity.internalServerError().body("Fehler beim Speichern des Produkts: " + e.getMessage());
        }
    }
}
