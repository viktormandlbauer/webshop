package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.AddressDto;
import at.fhtw.webshop.dto.receipt.ReceiptDto;
import at.fhtw.webshop.dto.receipt.ReceiptItemDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class ReceiptPdfService {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptPdfService.class);

    @Value("${receipts.output.path}")
    private String receiptsOutputPath;

    public void generateReceiptPdf(ReceiptDto receiptDto) {
        try {
            // Erstelle den Dateipfad
            String filePath = receiptsOutputPath + "/receipt_" + receiptDto.getOrderId() + ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Titel
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Quittung", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(Chunk.NEWLINE);

            // Bestellinformationen
            document.add(new Paragraph("Bestell-ID: " + receiptDto.getOrderId()));
            document.add(new Paragraph("Datum: " + receiptDto.getDate()));
            document.add(new Paragraph("Kunde: " + receiptDto.getCustomerName()));

            document.add(Chunk.NEWLINE);

            // Adressen
            document.add(new Paragraph("Rechnungsadresse:"));
            document.add(new Paragraph(formatAddress(receiptDto.getBillingAddress())));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Lieferadresse:"));
            document.add(new Paragraph(formatAddress(receiptDto.getShippingAddress())));
            document.add(Chunk.NEWLINE);

            // Artikel-Tabelle
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 1, 1, 1});

            addTableHeader(table);
            for (ReceiptItemDto item : receiptDto.getItems()) {
                addTableRow(table, item);
            }

            document.add(table);

            document.add(Chunk.NEWLINE);

            // Gesamtpreis
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph total = new Paragraph("Gesamtpreis: " + receiptDto.getTotal().setScale(2, RoundingMode.HALF_UP) + " €", totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Generieren der Quittung", e);
        }
    }

    private String formatAddress(AddressDto address) {
        return address.getStreetAddress() + ", " + address.getCity() + " " + address.getPostalCode() + ", " + address.getCountry();
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Produkt", "Menge", "Einzelpreis (€)", "Gesamt (€)")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(columnTitle));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private void addTableRow(PdfPTable table, ReceiptItemDto item) {
        table.addCell(item.getProductName());
        table.addCell(String.valueOf(item.getQuantity()));
        table.addCell(String.valueOf(item.getPricePerUnit()));
        table.addCell(String.valueOf(item.getSum()));
    }

    public Path getPdfPathForOrder(int orderId, int userId) {

        Path path = Paths.get("data/receipts", "receipt_" + orderId + ".pdf");
        logger.info("Retrieving PDF path {}", path);

        // Optional security check: ensure file belongs to user
        if (!Files.exists(path)) {
            throw new SecurityException("Unauthorized access or file not found.");
        }


        return path;
    }
}