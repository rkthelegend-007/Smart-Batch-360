package com.batching.view.export;

import com.batching.model.BatchData;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.List;

public class ExportBatchReport {

    public static void generateReport(List<BatchData> batchDataList, String filePath, List<String> selectedMaterials) {
        if (batchDataList == null || batchDataList.isEmpty()) return;

        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20); // landscape
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            BatchData header = batchDataList.get(0);

            // --- Title ---
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph title = new Paragraph("Batch Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // --- Company Header ---
            PdfPTable companyTable = new PdfPTable(2);
            companyTable.setWidthPercentage(100);
            companyTable.setWidths(new float[]{4, 1});

            PdfPCell leftBlock = new PdfPCell(new Phrase(
                    "Company Name : SmartBatch360\n" +
                            "Address : Pune\n" +
                            "Customer Address : " + header.getCustomerAdd() + "\n" +
                            "Site Address : " + header.getSiteAdd() + "\n" +
                            "City : Pune\n" +
                            "Pin Code : 410112\n" +
                            "GST Number : 56\n" +
                            "Recipe ID : " + header.getRecipeId() + "\n" +
                            "Order ID : " + header.getOrderId() + "\n" +
                            "Order Quantity : " + header.getOrderQuantity() + "\n" +
                            "Created At : " + header.getCreateDate(),
                    new Font(Font.FontFamily.HELVETICA, 10)
            ));
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

            addInfoRow(infoTable, "Customer Name", header.getCustomerName(), true);
            addInfoRow(infoTable, "Site Name", header.getSiteName(), true);
            addInfoRow(infoTable, "Batch Number", String.valueOf(header.getBatchNumber()), true);
            addInfoRow(infoTable, "Batch Date", header.getCycleDate() + " " + header.getCycleTime(), true);
            addInfoRow(infoTable, "Driver Name", header.getDriverName(), true);
            addInfoRow(infoTable, "Recipe Name", header.getRecipeName(), true);
            addInfoRow(infoTable, "Vehicle Number", header.getVehicleNumber(), true);
            addInfoRow(infoTable, "Batch Quantity", String.valueOf(header.getBatchQuantity()), true);
            addInfoRow(infoTable, "Shift", String.valueOf(header.getShiftNumber()), true);
            addInfoRow(infoTable, "PerCycle Quantity", String.valueOf(header.getPerCycleQuantity()), true);

            document.add(infoTable);
            document.add(new Paragraph(" "));

            // --- Main Table ---
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

            // Target & Setpoint rows
            addDynamicRow(mainTable, selectedMaterials, "Target", header.getTargetTotal(), BaseColor.LIGHT_GRAY, header);
            addDynamicRow(mainTable, selectedMaterials, "Set", header.getSetpointTotal(), BaseColor.LIGHT_GRAY, header);

            // --- Cycle Rows ---
            for (BatchData data : batchDataList) {
                mainTable.addCell(makeCell(String.valueOf(data.getCycleNumber()), headerFont, BaseColor.WHITE, Element.ALIGN_CENTER));
                mainTable.addCell(makeCell(data.getCycleTime() != null ? data.getCycleTime().toString() : "", headerFont, BaseColor.WHITE, Element.ALIGN_CENTER));

                double rowTotal = 0.0;
                for (String mat : selectedMaterials) {
                    try {
                        int index = getMaterialIndex(mat);
                        String methodName = "getMat" + index + "Achieved";
                        Method method = BatchData.class.getMethod(methodName);
                        String value = (String) method.invoke(data);

                        mainTable.addCell(makeCell(value, headerFont, BaseColor.WHITE, Element.ALIGN_RIGHT));
                        if (value != null && !value.isEmpty()) {
                            rowTotal += Double.parseDouble(value);
                        }
                    } catch (Exception ignored) {
                        mainTable.addCell(makeCell("", headerFont, BaseColor.WHITE, Element.ALIGN_RIGHT));
                    }
                }
                mainTable.addCell(makeCell(String.valueOf(rowTotal), headerFont, BaseColor.WHITE, Element.ALIGN_RIGHT));
            }

            // Achieved & Var %
            addDynamicRow(mainTable, selectedMaterials, "Achieved", header.getAchievedTotal(), BaseColor.LIGHT_GRAY, header);
            addDynamicRow(mainTable, selectedMaterials, "Var %", "0.0", BaseColor.LIGHT_GRAY, header);

            document.add(mainTable);

            // --- Footer ---
            PdfPTable footer = new PdfPTable(2);
            footer.setWidthPercentage(100);
            footer.setSpacingBefore(10);

            PdfPCell producedQty = new PdfPCell(new Phrase("Actual Produced Quantity : " + header.getAchievedTotal(), headerFont));
            producedQty.setBorder(Rectangle.NO_BORDER);
            footer.addCell(producedQty);

            PdfPCell batchInfo = new PdfPCell(new Phrase(
                    "Recipe ID : " + header.getRecipeId() + " | Order ID : " + header.getOrderId() +
                            " | Batch Hold Time : 0.00 Min.", headerFont
            ));
            batchInfo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            batchInfo.setBorder(Rectangle.NO_BORDER);
            footer.addCell(batchInfo);

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

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "", valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(4);
        table.addCell(valueCell);
    }

    // Corrected method signature to accept BatchData
    private static void addDynamicRow(PdfPTable table, List<String> selectedMaterials, String rowName, String totalValue, BaseColor bg, BatchData data) {
        Font font = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);

        table.addCell(makeCell("", font, bg, Element.ALIGN_CENTER));
        table.addCell(makeCell(rowName, font, bg, Element.ALIGN_LEFT));

        for (String mat : selectedMaterials) {
            String value = "";
            try {
                int index = getMaterialIndex(mat);
                String methodName = "";
                switch (rowName) {
                    case "Target": methodName = "getMat" + index + "Target"; break;
                    case "Set": methodName = "getMat" + index + "Setpoint"; break;
                    case "Achieved": methodName = "getMat" + index + "Achieved"; break;
                    default: break;
                }
                if (!methodName.isEmpty()) {
                    Method method = BatchData.class.getMethod(methodName);
                    value = (String) method.invoke(data);
                }
            } catch (Exception ignored) {
                // Handle cases where the material name or method doesn't exist.
            }
            table.addCell(makeCell(value, font, bg, Element.ALIGN_RIGHT));
        }

        table.addCell(makeCell(totalValue, font, bg, Element.ALIGN_RIGHT));
    }

    private static int getMaterialIndex(String matName) {
        try {
            return Integer.parseInt(matName.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            // Placeholder for a more robust lookup.
            return 0; 
        }
    }
    
    private static PdfPCell makeCell(String text, Font font, BaseColor bg, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
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