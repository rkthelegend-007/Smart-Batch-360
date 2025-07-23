package com.batching.util;

import java.io.FileOutputStream;

import com.batching.model.CycleData;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.batching.model.CycleData;

import java.io.FileOutputStream;
import java.util.List;

public class PdfGenerator {
    
    public static void generatePdf(List<CycleData> cycleDataList, String filePath) throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Batching Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(22);
        table.setWidthPercentage(100);

        String[] headers = {"Sr_No", "Batch", "M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10",
                "M11", "M12", "M13", "M14", "M15", "M16", "M17", "M18", "M19", "M20"};

        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }

        for (CycleData data : cycleDataList) {
            table.addCell(String.valueOf(data.getSrNo()));
            table.addCell(data.getBatchNumber());
            table.addCell(data.getMaterial1());
            table.addCell(data.getMaterial2());
            table.addCell(data.getMaterial3());
            table.addCell(data.getMaterial4());
            table.addCell(data.getMaterial5());
            table.addCell(data.getMaterial6());
            table.addCell(data.getMaterial7());
            table.addCell(data.getMaterial8());
            table.addCell(data.getMaterial9());
            table.addCell(data.getMaterial10());
            table.addCell(data.getMaterial11());
            table.addCell(data.getMaterial12());
            table.addCell(data.getMaterial13());
            table.addCell(data.getMaterial14());
            table.addCell(data.getMaterial15());
            table.addCell(data.getMaterial16());
            table.addCell(data.getMaterial17());
            table.addCell(data.getMaterial18());
            table.addCell(data.getMaterial19());
            table.addCell(data.getMaterial20());
        }

        document.add(table);
        document.close();
    }
}
