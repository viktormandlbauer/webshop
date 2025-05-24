package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.ReceiptDto;
import at.fhtw.webshop.dto.AddressDto;
import at.fhtw.webshop.dto.OrderItemDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

@Service
public class ReceiptPdfService {

    @Value("${receipts.output.path:receipts}")
    private String receiptFolder;

    public File generateAndStorePdf(ReceiptDto receiptDto) {
        ByteArrayOutputStream out = createPdfContent(receiptDto);

        try {
            Path dirPath = Path.of(receiptFolder);
            if (Files.notExists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String fileName = "receipt_order_" + receiptDto.getOrderId() + ".pdf";
            Path filePath = dirPath.resolve(fileName);

            Files.write(filePath, out.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return filePath.toFile();

        } catch (Exception e) {
            throw new RuntimeException("Failed to store receipt PDF", e);
        }
    }

    public ByteArrayInputStream generatePdf(ReceiptDto receiptDto) {
        ByteArrayOutputStream out = createPdfContent(receiptDto);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private ByteArrayOutputStream createPdfContent(ReceiptDto receiptDto) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

            document.add(new Paragraph("Order Receipt", titleFont));
            document.add(new Paragraph("Order ID: " + receiptDto.getOrderId(), textFont));
            document.add(new Paragraph("Date: " + receiptDto.getDate(), textFont));
            document.add(new Paragraph("Customer: " + receiptDto.getCustomerName(), textFont));
            document.add(new Paragraph(" "));

            AddressDto address = receiptDto.getShippingAddress();
            document.add(new Paragraph("Shipping Address:", sectionFont));
            document.add(new Paragraph(address.getStreetAddress(), textFont));
            document.add(new Paragraph(address.getPostalCode() + " " + address.getCity(), textFont));
            document.add(new Paragraph(address.getCountry(), textFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{5, 1, 2, 2});
            addTableHeader(table, sectionFont);

            for (OrderItemDto item : receiptDto.getItems()) {
                table.addCell(item.getProductName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell("€" + item.getPricePerUnit().setScale(2, RoundingMode.HALF_UP));
                table.addCell("€" + item.getTotalPrice().setScale(2, RoundingMode.HALF_UP));
            }

            document.add(table);
            document.add(new Paragraph("\nTotal: €" + receiptDto.getTotal().setScale(2, RoundingMode.HALF_UP), sectionFont));
            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate receipt PDF", e);
        }

        return out;
    }

    private void addTableHeader(PdfPTable table, Font font) {
        Stream.of("Product", "Qty", "Unit Price", "Total")
                .forEach(title -> {
                    PdfPCell header = new PdfPCell(new Phrase(title, font));
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    table.addCell(header);
                });
    }
}
