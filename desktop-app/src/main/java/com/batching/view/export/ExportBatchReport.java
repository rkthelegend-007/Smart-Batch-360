package com.batching.view.export;

import com.batching.model.BatchData;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.util.List;

public class ExportBatchReport {

    public static void generateReport(List<BatchData> batchDataList, String filePath, List<String> selectedMaterials) {
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20); // landscape
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // --- Report Title ---
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph title = new Paragraph("Batch Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // --- Company Header (Gray Background + Logo) ---
            PdfPTable companyTable = new PdfPTable(2);
            companyTable.setWidthPercentage(100);
            companyTable.setWidths(new float[]{4, 1});

            PdfPCell leftBlock = new PdfPCell(new Phrase(
                    "Company Name : Gainwell\n" +
                            "Address : Pune\n" +
                            ":\n" +
                            ":\n" +
                            "City : Pune\n" +
                            "Pin Code : 410112\n" +
                            "GST Number : 56",
                    new Font(Font.FontFamily.HELVETICA, 10)));
            leftBlock.setBackgroundColor(BaseColor.LIGHT_GRAY);
            leftBlock.setBorder(Rectangle.NO_BORDER);
            leftBlock.setPadding(6);
            companyTable.addCell(leftBlock);

            PdfPCell logoBlock = new PdfPCell(new Phrase("Company Logo"));
            logoBlock.setBackgroundColor(BaseColor.LIGHT_GRAY);
            logoBlock.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoBlock.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoBlock.setBorder(Rectangle.NO_BORDER);
            companyTable.addCell(logoBlock);

            document.add(companyTable);
            document.add(new Paragraph(" "));

            // --- Customer / Site Info ---
            PdfPTable infoTable = new PdfPTable(4);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{2, 3, 2, 3});

            addInfoRow(infoTable, "Customer Name", "GainWell", true);
            addInfoRow(infoTable, "Site Name", "SKY SALES", true);

            addInfoRow(infoTable, "Batch Number", "11", true);
            addInfoRow(infoTable, "Batch Date", "11-06-2025 13:02:28", true);

            addInfoRow(infoTable, "Driver Name", "RRR", true);
            addInfoRow(infoTable, "Recipe Name", "Test 4", true);

            addInfoRow(infoTable, "Vehicle Number", "MH12FF420", true);
            addInfoRow(infoTable, "Batch Quantity", "3.00", true);

            addInfoRow(infoTable, "BOM", "0", true);
            addInfoRow(infoTable, "PerCycle Quantity", "1.00", true);

            document.add(infoTable);
            document.add(new Paragraph(" "));

            // --- Main Table ---
            // Always start with No + Time
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
            PdfPTable mainTable = new PdfPTable(selectedMaterials.size() + 3); // No + Time + Materials + TOTAL
            mainTable.setWidthPercentage(100);

            // Header row
            mainTable.addCell(makeHeaderCell("No", headerFont));
            mainTable.addCell(makeHeaderCell("Time", headerFont));
            for (String mat : selectedMaterials) {
                mainTable.addCell(makeHeaderCell(mat, headerFont));
            }
            mainTable.addCell(makeHeaderCell("TOTAL", headerFont));

            // Target row
            addDynamicRow(mainTable, selectedMaterials, "Target", "200", BaseColor.LIGHT_GRAY);
            // Set row
            addDynamicRow(mainTable, selectedMaterials, "Set", "200", BaseColor.LIGHT_GRAY);

            // Example cycles
            for (int i = 1; i <= 3; i++) {
                mainTable.addCell(makeCell(String.valueOf(i), headerFont, BaseColor.WHITE, Element.ALIGN_CENTER));
                mainTable.addCell(makeCell("17:20:" + (50 + i), headerFont, BaseColor.WHITE, Element.ALIGN_CENTER));
                for (String mat : selectedMaterials) {
                    mainTable.addCell(makeCell("222", headerFont, BaseColor.WHITE, Element.ALIGN_RIGHT));
                }
                mainTable.addCell(makeCell("1530", headerFont, BaseColor.WHITE, Element.ALIGN_RIGHT));
            }

            // Achieved row
            addDynamicRow(mainTable, selectedMaterials, "Achieved", "600", BaseColor.LIGHT_GRAY);
            // Set total row
            addDynamicRow(mainTable, selectedMaterials, "Set", "600", BaseColor.LIGHT_GRAY);
            // Var % row
            addDynamicRow(mainTable, selectedMaterials, "Var %", "10.8", BaseColor.LIGHT_GRAY);

            document.add(mainTable);

            // --- Footer ---
            PdfPTable footer = new PdfPTable(2);
            footer.setWidthPercentage(100);
            footer.setSpacingBefore(10);

            PdfPCell producedQty = new PdfPCell(new Phrase("Actual Produced Quantity : 3.91 TON", headerFont));
            producedQty.setBorder(Rectangle.NO_BORDER);
            footer.addCell(producedQty);

            PdfPCell holdTime = new PdfPCell(new Phrase("Batch Hold Time : 0.00 Min.", headerFont));
            holdTime.setHorizontalAlignment(Element.ALIGN_RIGHT);
            holdTime.setBorder(Rectangle.NO_BORDER);
            footer.addCell(holdTime);

            document.add(footer);

            document.close();
            System.out.println("✅ PDF generated at: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Helpers ---
    private static void addInfoRow(PdfPTable table, String label, String value, boolean bold) {
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 9, bold ? Font.BOLD : Font.NORMAL);

        PdfPCell labelCell = new PdfPCell(new Phrase(label + " :", labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(4);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(4);
        table.addCell(valueCell);
    }

    private static void addDynamicRow(PdfPTable table, List<String> selectedMaterials, String rowName, String value, BaseColor bg) {
        Font font = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);

        table.addCell(makeCell("", font, bg, Element.ALIGN_CENTER));
        table.addCell(makeCell(rowName, font, bg, Element.ALIGN_LEFT));
        for (String mat : selectedMaterials) {
            table.addCell(makeCell(value, font, bg, Element.ALIGN_RIGHT));
        }
        table.addCell(makeCell("1234", font, bg, Element.ALIGN_RIGHT));
    }

    private static PdfPCell makeCell(String text, Font font, BaseColor bg, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(align);
        cell.setBackgroundColor(bg);
        cell.setPadding(4);
        return cell;
    }

    private static PdfPCell makeHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(4);
        return cell;
    }
}
