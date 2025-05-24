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

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private static final Logger logger = LoggerFactory.getLogger(OrderRestController.class);

    private final ReceiptPdfService receiptPdfService;
    private final OrderService orderService;

    public OrderRestController(ReceiptPdfService receiptPdfService, OrderService orderService) {
        this.receiptPdfService = receiptPdfService;
        this.orderService = orderService;
    }

    /**
    @GetMapping("/all")
    public List<OrderDto> getAllOrders(@AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {

        // @TODO: This mapping should be in the /api/admin route

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return orderService.getAllOrders();
        } else {
            logger.warn("Unauthorized access attempt by user: {}", userDetails.getUsername());
            return List.of(); // Return an empty list if not authorized
        }
    }
    @GetMapping("/user")
    public List<OrderDto> getOrdersForUser(@AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        return orderService.getOrderForUser(userDetails.getUsername());
    }
     **/

    @PostMapping("/new")
    public ResponseEntity<Object> newOrder(@Valid @RequestBody CustomerOrderDto customerOrderDto, @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails){
        try {
            orderService.newCustomerOrder(customerOrderDto, userDetails);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Order successfully"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to order: " + e.getMessage()
            ));
        }
    }


    /**
    @PostMapping(value = "/receipt/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReceiptPdf(@RequestBody ReceiptDto receiptDto) throws IOException {
        ByteArrayInputStream pdfStream = receiptPdfService.generatePdf(receiptDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=receipt_order_" + receiptDto.getOrderId() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfStream.readAllBytes());
    }

    @PostMapping("/receipt/store")
    public ResponseEntity<String> generateAndStoreReceipt(@RequestBody ReceiptDto receiptDto) {
        File storedFile = receiptPdfService.generateAndStorePdf(receiptDto);
        return ResponseEntity.ok("Receipt stored at: " + storedFile.getAbsolutePath());
    }

    @GetMapping(value = "/receipt/download/{orderId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadStoredReceipt(@PathVariable Long orderId) throws IOException {
        File file = new File("receipts/receipt_order_" + orderId + ".pdf");
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        FileInputStream fis = new FileInputStream(file);
        byte[] content = fis.readAllBytes();
        fis.close();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + file.getName());

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(content);
    }
    **/
}
