package org.example.myspringapp.Service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PDFService {

    // Method to generate PDF with company name, current date, and service details
    public byte[] generatePdf(String serviceName, double servicePrice) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument)) {

            // Add company name to the PDF
            document.add(new Paragraph("Service Provider SARL"));

            // Add current date to the PDF
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            document.add(new Paragraph("Date: " + currentDate.format(formatter)));

            // Add service details to the PDF
            document.add(new Paragraph("Service Name: " + serviceName));
            document.add(new Paragraph("Service Price: " + servicePrice));

            // Add more content as needed
            // You can add additional information or styling here
        }

        return outputStream.toByteArray();
    }
}
