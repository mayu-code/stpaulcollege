package com.main.stpaul.helper;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import com.main.stpaul.dto.response.ReceiptResponse;
import com.main.stpaul.entities.PaymentDetail;
import com.main.stpaul.entities.Student;

import java.io.ByteArrayOutputStream;

public class PdfGenerator {

    public static byte[] generateReceiptPdf(Student student, ReceiptResponse receipt, PaymentDetail payment) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            Table outerTable = new Table(1).useAllAvailableWidth();
            outerTable.setBorder(new SolidBorder(ColorConstants.BLACK, 2f));

            outerTable.addCell(getCell(new Paragraph("VIVEKSHIL MITRA PARIWAR INSTITUTION\nST. PAUL SCHOOL (CBSE)")
                    .setFont(boldFont)
                    .setTextAlignment(TextAlignment.CENTER), true));

            outerTable.addCell(getCell(new Paragraph("TO BE AFFILIATED CBSE DELHI\nNEAR BRIDGE, HUDKESHWAR, NAGPUR-440034")
                    .setFont(regularFont)
                    .setTextAlignment(TextAlignment.CENTER), true));

            outerTable.addCell(getCell(new Paragraph("RECEIPT").setFont(boldFont).setTextAlignment(TextAlignment.CENTER), true));

            Table detailsTable = new Table(new float[]{3, 5, 3, 5}).useAllAvailableWidth();
            detailsTable.addCell(getBorderedCell("Receipt No:", boldFont));
            detailsTable.addCell(getBorderedCell(getValueOrDefault(receipt.getReceiptNo()), regularFont));
            detailsTable.addCell(getBorderedCell("Date:", boldFont));
            detailsTable.addCell(getBorderedCell(getValueOrDefault(receipt.getPaymentDate()), regularFont));

            detailsTable.addCell(getBorderedCell("Name:", boldFont));
            detailsTable.addCell(getBorderedCell(getValueOrDefault(student.getFirstName() + " " + student.getSurname()), regularFont));
            detailsTable.addCell(getBorderedCell("Std:", boldFont));
            detailsTable.addCell(getBorderedCell(getValueOrDefault(student.getStdClass()), regularFont));

            outerTable.addCell(new Cell().add(detailsTable).setBorder(new SolidBorder(ColorConstants.BLACK, 1f)));

            Table feeTable = new Table(new float[]{1, 5, 3}).useAllAvailableWidth();
            feeTable.setBorder(new SolidBorder(ColorConstants.BLACK, 1f));

            feeTable.addCell(getBorderedCell("Sr. No", boldFont));
            feeTable.addCell(getBorderedCell("PARTICULARS", boldFont));
            feeTable.addCell(getBorderedCell("Amount", boldFont));

            addFeeRow(feeTable, "1.", "Admission Fee", "");
            addFeeRow(feeTable, "2.", "Prospectus Fee", "");
            addFeeRow(feeTable, "3.", "Tuition Fee", String.valueOf(receipt.getAmountPaid()));
            addFeeRow(feeTable, "4.", "Previous Dues", "");
            addFeeRow(feeTable, "5.", "Other Fee", "");

            feeTable.addCell(getBorderedCell("", regularFont));
            feeTable.addCell(getBorderedCell("Total", boldFont));
            feeTable.addCell(getBorderedCell(getValueOrDefault(receipt.getAmountPaid()), boldFont));

            outerTable.addCell(new Cell().add(feeTable).setBorder(new SolidBorder(ColorConstants.BLACK, 1f)));

            outerTable.addCell(new Cell().add(new Paragraph("Amount (In words) " + NumberToWordConverter.convert(receipt.getAmountPaid()) + " ONLY").setFont(boldFont))
                    .setBorder(new SolidBorder(ColorConstants.BLACK, 1f)));

            Table signTable = new Table(new float[]{1, 1, 1}).useAllAvailableWidth();
            signTable.addCell(getSignatureCell("Clerk"));
            signTable.addCell(getSignatureCell("Cashier"));
            signTable.addCell(getSignatureCell("Accountant"));

            outerTable.addCell(new Cell().add(signTable).setBorder(new SolidBorder(ColorConstants.BLACK, 1f)));

            document.add(outerTable);
            document.close();

            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getValueOrDefault(Object value) {
        return (value == null || value.toString().trim().isEmpty()) ? "-" : value.toString();
    }

    private static Cell getBorderedCell(String text, PdfFont font) {
        return new Cell().add(new Paragraph(text).setFont(font))
                .setBorder(new SolidBorder(ColorConstants.BLACK, 1f))
                .setPadding(5)
                .setTextAlignment(TextAlignment.LEFT);
    }

    private static Cell getCell(Paragraph paragraph, boolean border) {
        return new Cell().add(paragraph)
                .setBorder(border ? new SolidBorder(ColorConstants.BLACK, 1f) : SolidBorder.NO_BORDER)
                .setPadding(5);
    }

    private static void addFeeRow(Table table, String srNo, String particulars, String amount) throws Exception {
        table.addCell(getBorderedCell(srNo, PdfFontFactory.createFont(StandardFonts.HELVETICA)));
        table.addCell(getBorderedCell(particulars, PdfFontFactory.createFont(StandardFonts.HELVETICA)));
        table.addCell(getBorderedCell(amount, PdfFontFactory.createFont(StandardFonts.HELVETICA)));
    }

    private static Cell getSignatureCell(String text) throws Exception {
        return new Cell().add(new Paragraph(text).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(10)
                .setBorder(Border.NO_BORDER);
    }
}