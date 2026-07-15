package project.document.domain.excel.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    public void writeExcelSheetToWorkbook(Workbook workbook, String sheetName, Map<String, String> cellValues, int minWidth, List<Map<String, Object>> dataList) {
		Sheet sheet = workbook.createSheet(sheetName);
		createHeaderRow(sheet, cellValues);

		CellStyle integerStyle = configCellStyleNumeric(workbook, false);
		CellStyle decimalStyle = configCellStyleNumeric(workbook, true);

		for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
			createDataRow(sheet, rowIndex + 1, dataList.get(rowIndex), cellValues, integerStyle, decimalStyle);
		}

		adjustColumnWidth(cellValues, minWidth, sheet);
	}

	private void createHeaderRow(Sheet sheet, Map<String, String> cellValues) {
		Row headerRow = sheet.createRow(0);
		int cellIndex = 0;
		for (String header : cellValues.values()) {
			headerRow.createCell(cellIndex++).setCellValue(header);
		}
	}

	private void createDataRow(Sheet sheet, int rowNum, Map<String, Object> dataMap, Map<String, String> cellValues, CellStyle integerStyle, CellStyle decimalStyle) {
		Row dataRow = sheet.createRow(rowNum);
		int cellIndex = 0;
		for (String key : cellValues.keySet()) {
			Cell cell = dataRow.createCell(cellIndex++);
			Object value = dataMap.get(key);
			setCellValue(cell, value, integerStyle, decimalStyle);
		}
	}

	private void setCellValue(Cell cell, Object value, CellStyle integerStyle, CellStyle decimalStyle) {
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof String stringValue) {
			cell.setCellValue(stringValue);
		} else if (value instanceof Number numValue) {
			if (value instanceof Integer || value instanceof Long) {
				cell.setCellValue(numValue.longValue());
				cell.setCellStyle(integerStyle);
			} else {
				cell.setCellValue(numValue.doubleValue());
				cell.setCellStyle(decimalStyle);
			}
		} else {
			cell.setCellValue(value.toString());
		}
	}
}