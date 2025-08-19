package com.batching.model;

import java.io.FileOutputStream;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.List;
import com.itextpdf.text.*;

public class ExportBatchReport {
   
    public static void generateReport(List<BatchData> batchDataList, String filePath) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Report Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Batch Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" ")); // empty line

            // Table with some important columns
            PdfPTable table = new PdfPTable(6); // adjust number of columns
            table.setWidthPercentage(100);

            // Table Header
            table.addCell("Sr No");
            table.addCell("Batch No");
            table.addCell("Customer");
            table.addCell("Site");
            table.addCell("Driver");
            table.addCell("Quantity");

            // Data Rows
            for (BatchData data : batchDataList) {
                table.addCell(String.valueOf(data.getSrNo()));
                table.addCell(String.valueOf(data.getBatchNumber()));
                table.addCell(data.getCustomerName());
                table.addCell(data.getSiteName());
                table.addCell(data.getDriverName());
                table.addCell(String.valueOf(data.getBatchQuantity()));
            }

            document.add(table);

            document.close();
            System.out.println("✅ PDF generated at: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
