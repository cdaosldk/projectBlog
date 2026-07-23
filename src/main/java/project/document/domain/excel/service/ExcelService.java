package project.document.domain.excel.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import project.document.domain.excel.annotation.ExcelColumn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    private static final int MAX_ROWS_PER_SHEET = 1048500;
    private static final int ROW_ACCESS_WINDOW_SIZE = 100;
    private static final String STYLE_INTEGER = "integer";
    private static final String STYLE_DECIMAL = "decimal";
    private static final String STYLE_HEADER = "header";

    public <T> byte[] generateExcelFile(String sheetName, int minWidth, List<T> dataList) throws IOException, IllegalAccessException {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("데이터 목록이 비어있습니다.");
        }

        // 1. 대상 클래스의 필드 중 @ExcelColumn 애너테이션이 붙은 필드 추출 및 정렬
        Class<?> clazz = dataList.getFirst().getClass();
        List<Field> excelFields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(ExcelColumn.class).order()))
                .toList();

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            workbook.setCompressTempFiles(true);
            Map<String, CellStyle> styles = createCommonCellStyles(workbook);

            Sheet currentSheet = null;
            int currentSheetIndex = 1;
            int currentRowNum = 0;

            for (T data : dataList) {
                if (currentSheet == null || currentRowNum >= MAX_ROWS_PER_SHEET) {
                    if (currentSheet != null) {
                        adjustColumnWidth(excelFields, minWidth, currentSheet);
                    }

                    String sheetNameNew = currentSheetIndex == 1 ? sheetName : sheetName + "_" + currentSheetIndex;
                    currentSheet = workbook.createSheet(sheetNameNew);

                    createHeaderRow(currentSheet, excelFields, styles.get(STYLE_HEADER));
                    currentRowNum = 1;
                    currentSheetIndex++;
                }

                createDataRow(currentSheet, currentRowNum++, data, excelFields, styles);
            }

            if (currentSheet != null) {
                adjustColumnWidth(excelFields, minWidth, currentSheet);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private Map<String, CellStyle> createCommonCellStyles(Workbook workbook) {
        Map<String, CellStyle> styles = new HashMap<>();

        // Header Style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        styles.put(STYLE_HEADER, headerStyle);

        // Integer Style
        CellStyle integerStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        integerStyle.setDataFormat(format.getFormat("#,##0"));
        styles.put(STYLE_INTEGER, integerStyle);

        // Decimal Style
        CellStyle decimalStyle = workbook.createCellStyle();
        decimalStyle.setDataFormat(format.getFormat("#,##0.00"));
        styles.put(STYLE_DECIMAL, decimalStyle);

        return styles;
    }

    private void createHeaderRow(Sheet sheet, List<Field> fields, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        int cellIndex = 0;
        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            Cell cell = headerRow.createCell(cellIndex++);
            cell.setCellValue(annotation.headerName());
            cell.setCellStyle(headerStyle);
        }
    }

    private <T> void createDataRow(Sheet sheet, int rowNum, T data, List<Field> fields, Map<String, CellStyle> styles) throws IllegalAccessException {
        Row dataRow = sheet.createRow(rowNum);
        int cellIndex = 0;
        for (Field field : fields) {
            Cell cell = dataRow.createCell(cellIndex++);
            Object value = field.get(data); // 리플렉션을 활용해 런타임에 필드값 획득
            setCellValue(cell, value, styles);
        }
    }

    private void setCellValue(Cell cell, Object value, Map<String, CellStyle> styles) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String stringValue) {
            cell.setCellValue(stringValue);
        } else if (value instanceof Number numValue) {
            if (value instanceof Integer || value instanceof Long) {
                cell.setCellValue(numValue.longValue());
                cell.setCellStyle(styles.get(STYLE_INTEGER));
            } else {
                cell.setCellValue(numValue.doubleValue());
                cell.setCellStyle(styles.get(STYLE_DECIMAL));
            }
        } else {
            cell.setCellValue(value.toString());
        }
    }

    private void adjustColumnWidth(List<Field> fields, int minWidth, Sheet sheet) {
        int colIndex = 0;
        for (Field field : fields) {
            String headerName = field.getAnnotation(ExcelColumn.class).headerName();
            int calculatedWidth = Math.max(minWidth, headerName.length() * 2 + 5);
            sheet.setColumnWidth(colIndex, calculatedWidth * 256);
            colIndex++;
        }
    }
}
