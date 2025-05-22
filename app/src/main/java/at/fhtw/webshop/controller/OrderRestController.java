package at.fhtw.webshop.controller;

import at.fhtw.webshop.dto.OrderDto;
import at.fhtw.webshop.dto.ReceiptDto;
import at.fhtw.webshop.service.OrderService;
import at.fhtw.webshop.service.ReceiptPdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

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

    @GetMapping
    public List<OrderDto> getOrdersForUser(@AuthenticationPrincipal UserDetails userDetails) {
        int userId = Integer.parseInt(userDetails.getUsername());
        return orderService.getOrdersForCurrentUser(userId);
    }

    /**
     * Generate a receipt PDF and return it inline for viewing/downloading.
     */
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

    /**
     * Generate a receipt PDF and store it on the server.
     */
    @PostMapping("/receipt/store")
    public ResponseEntity<String> generateAndStoreReceipt(@RequestBody ReceiptDto receiptDto) {
        File storedFile = receiptPdfService.generateAndStorePdf(receiptDto);
        return ResponseEntity.ok("Receipt stored at: " + storedFile.getAbsolutePath());
    }

    /**
     * Download a previously stored receipt by order ID.
     */
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
}
