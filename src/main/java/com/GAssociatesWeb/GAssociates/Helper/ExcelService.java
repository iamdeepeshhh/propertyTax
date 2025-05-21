package com.GAssociatesWeb.GAssociates.Helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelService {
    private static final String[] COLUMN_HEADERS = {
            "Old Property Number", "Ward Number", "Zone", "Owner Name", "Occupier Name",
            "Property Address", "Construction Class", "Property Type", "Property Subtype",
            "Usage Type", "Number of Rooms", "Plot Value", "Total Property Value",
            "Building Value", "Built-Up Area", "Last Assessment Date", "Current Assessment Date",
            "Total Tax", "Property Tax", "Education Cess", "EGC", "Tree Tax", "Environment Tax",
            "Fire Tax", "New Property Number", "Construction Year", "User ID", "Total Assessment Area",
            "Total Ratable Value","Total Assessment Area ft"
    };

    public byte[] generateExcelTemplate() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Old Property Details");

        // Create a header row
        Row headerRow = sheet.createRow(0);

        // Create header cells
        for (int i = 0; i < COLUMN_HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(COLUMN_HEADERS[i]);
            CellStyle headerCellStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerCellStyle.setFont(headerFont);
            cell.setCellStyle(headerCellStyle);
        }

        // Adjust column widths
        for (int i = 0; i < COLUMN_HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the workbook to a byte array output stream
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return out.toByteArray();
        }
    }
}