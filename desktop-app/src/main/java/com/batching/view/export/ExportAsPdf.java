package com.batching.view.export;

import com.batching.model.CycleData;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExportAsPdf {

    public static void showPreviewAndExport(List<CycleData> dataList, Stage ownerStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF Report");
        fileChooser.setInitialFileName("BatchingReport.pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(ownerStage);

        if (file != null) {
            try {
                generatePdf(dataList, file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void generatePdf(List<CycleData> cycleDataList, String filePath) throws Exception {
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 50, 40); // Landscape A4 with tighter margins
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        writer.setPageEvent(new PdfHeaderFooter());

        document.open();

        // Add logo
        try {
            Image logo = Image.getInstance("src/main/resources/logo.png");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Image.ALIGN_LEFT);
            document.add(logo);
        } catch (Exception e) {
            System.out.println("Logo not found or error loading logo.");
        }

        document.add(Chunk.NEWLINE);

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Batch Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(22);
        table.setWidthPercentage(100);
        table.setWidths(new float[] {
            1.2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f,
            2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f
        });

        String[] headers = {"Sr_No", "Batch", "M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10",
                "M11", "M12", "M13", "M14", "M15", "M16", "M17", "M18", "M19", "M20"};

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(4f);
            table.addCell(cell);
        }

        for (CycleData data : cycleDataList) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(data.getSrNo()), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getBatchNumber(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial1(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial2(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial3(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial4(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial5(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial6(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial7(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial8(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial9(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial10(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial11(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial12(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial13(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial14(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial15(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial16(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial17(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial18(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial19(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(data.getMaterial20(), dataFont)));
        }

        document.add(table);
        document.close();
    }

    // Header/Footer inner class
    static class PdfHeaderFooter extends PdfPageEventHelper {
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, BaseColor.GRAY);

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable footer = new PdfPTable(1);
            try {
                footer.setTotalWidth(523);
                footer.setWidthPercentage(100);
                footer.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell = new PdfPCell(new Phrase("Generated by Batching Report App - © RK India", footerFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                footer.addCell(cell);

                footer.writeSelectedRows(0, -1, 36, 40, writer.getDirectContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
