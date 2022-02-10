package ru.progressnw.employees.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResponsibilityExcelExporter {
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<Responsibility> listResponsibilityDescription;

    public ResponsibilityExcelExporter(List<Responsibility> listResponsibilityDescription) {
        this.listResponsibilityDescription = listResponsibilityDescription;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Обязанности");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        Set<User> filteredusers = new HashSet<>();
        listResponsibilityDescription.forEach(responsibility -> filteredusers.add(responsibility.getUser()));
        StringBuilder sb = new StringBuilder();
        sb.append("Обязанности сотрудников - ");
        filteredusers.forEach(user -> sb.append(user.getLastname()).append(' ').append(user.getFirstname()).append(';'));
        createCell(row, 0, sb.toString(), style);
    }

    private void createCell(Row row, int columnCount, String value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();

        for (Responsibility responsibility : listResponsibilityDescription) {
            Row row = sheet.createRow(rowCount++);
            createCell(row, 0, responsibility.getDescription(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
