package at.fhtw.webshop.controller.rest;

import at.fhtw.webshop.dto.CustomerOrderDto;
import at.fhtw.webshop.security.CustomUserDetails;
import at.fhtw.webshop.service.OrderService;
import at.fhtw.webshop.service.ReceiptPdfService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private static final Logger logger = LoggerFactory.getLogger(OrderRestController.class);

    private final OrderService orderService;
    private final ReceiptPdfService receiptPdfService;

    public OrderRestController(OrderService orderService, ReceiptPdfService receiptPdfService) {
        this.orderService = orderService;
        this.receiptPdfService = receiptPdfService;
    }

    @GetMapping
    public ResponseEntity<Object> getOrdersForUser(@AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", orderService.getOrdersForUser(userDetails)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to retrieve orders: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Object> newOrder(@Valid @RequestBody CustomerOrderDto customerOrderDto, @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails){
        try {
            int orderId = orderService.newCustomerOrder(customerOrderDto, userDetails);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Order successfully",
                    "orderId", orderId
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to order: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/receipt")
    public ResponseEntity<byte[]> getReceiptPdf(
            @RequestParam Integer orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            // Get the path to the generated PDF
            Path pdfPath = receiptPdfService.getPdfPathForOrder(orderId, userDetails.getId());

            if (!Files.exists(pdfPath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            byte[] pdfBytes = Files.readAllBytes(pdfPath);

            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "inline; filename=receipt_" + orderId + ".pdf")
                    .body(pdfBytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }
    }


}
