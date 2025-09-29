package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.ReportGenerator.AssessmentResultsReportGenerator;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class AssessmentReportPdfGeneratorImpl implements AssessmentReportPdfGenerator {

    @Override
    public byte[] generateCombinedWarningsPdf(String wardNo, List<AssessmentResultsDto> assessmentResultsDtos){
        System.out.println("📝 PDF Generation: Received " + assessmentResultsDtos.size() + " assessment results");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36); // Margins
        PdfWriter.getInstance(document, out);
        document.open();

        Font headerFont = new Font(Font.HELVETICA, 16, Font.BOLD, new Color(0, 51, 102));
        Font labelFont = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(0, 51, 102));
        Font redFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.RED);
        Font normalFont = new Font(Font.HELVETICA, 12);

        document.add(new Paragraph("Combined Property Assessment Warnings", headerFont));
        document.add(new Paragraph("Ward No: " + wardNo, normalFont));
        document.add(Chunk.NEWLINE);

        for (AssessmentResultsDto dto : assessmentResultsDtos) {
            PdfPTable table = new PdfPTable(3); // 3 columns: Prop No, Final No, Warnings
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{2f, 2f, 6f});

            // Add Header Row
            addCellToTable(table, "Property No", labelFont);
            addCellToTable(table, "Final Property No", labelFont);
            addCellToTable(table, "Warnings", labelFont);

            // Add Data Row
            addCellToTable(table, dto.getPdNewpropertynoVc(), normalFont);
            addCellToTable(table, dto.getPdFinalpropnoVc(), redFont);

            StringBuilder warningsText = new StringBuilder();
            if (dto.getWarnings() != null && !dto.getWarnings().isEmpty()) {
                for (String warning : dto.getWarnings()) {
                    warningsText.append("• ").append(warning).append("\n");
                }
            } else {
                warningsText.append("No warnings");
            }
            addCellToTable(table, warningsText.toString(), redFont);

            document.add(table);
        }

        document.close();
        return out.toByteArray();
    }

    private void addCellToTable(PdfPTable table, String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(cell);
    }
}