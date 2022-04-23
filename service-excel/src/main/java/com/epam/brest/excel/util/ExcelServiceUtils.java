package com.epam.brest.excel.util;

import com.epam.brest.excel.config.CellSettings;
import com.epam.brest.excel.config.ExcelSettings;
import com.epam.brest.excel.impl.BankAccountDtoExcelServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND;

public class ExcelServiceUtils {

    private static final Logger LOGGER = LogManager.getLogger(ExcelServiceUtils.class);

    private ExcelServiceUtils() {
    }

    public static void fillInRow(Row row, Object[] values, Map<Class<?>, CellStyle> styles) {
        LOGGER.trace("fillInRow(row={}, values={}, styles={})", row, values, styles);
        IntStream.range(0, values.length)
                 .forEach(i -> {
                     Cell cell = row.createCell(i);
                     if (values[i] instanceof String) {
                         cell.setCellValue((String) values[i]);
                         setCellStyle(cell, styles.get(values[i].getClass()));
                         return;
                     }
                     if (values[i] instanceof LocalDate) {
                         cell.setCellValue((LocalDate) values[i]);
                         setCellStyle(cell, styles.get(values[i].getClass()));
                         return;
                     }
                     if (values[i] instanceof Integer) {
                         cell.setCellValue((Integer) values[i]);
                         setCellStyle(cell, styles.get(values[i].getClass()));
                     }
                 });
    }

    public static Sheet createSheet(Workbook workbook, ExcelSettings excelSettings) {
        LOGGER.trace("createSheet(workbook={}, excelSettings={})", workbook, excelSettings);
        Sheet sheet = workbook.createSheet(excelSettings.getSheet());
        String[] headerCells = excelSettings.getHeaderCells();
        IntStream.range(0, headerCells.length)
                 .forEach(i -> sheet.setColumnWidth(i, headerCells[i].length() * Units.MASTER_DPI));
        return sheet;
    }

    public static CellStyle createCellStyle(Workbook workbook, CellSettings cellSettings) {
        LOGGER.trace("createCellStyle(workbook={}, cellSettings={})", workbook, cellSettings);
        CellStyle style = createBorderedStyle(workbook);
        Font font = createFont(workbook, cellSettings);
        style.setFont(font);
        style.setAlignment(cellSettings.getAlignment());
        style.setFillForegroundColor(cellSettings.getForegroundColor().getIndex());
        style.setFillPattern(SOLID_FOREGROUND);
        return style;
    }

    private static CellStyle createBorderedStyle(Workbook workbook){
        LOGGER.trace("createCellStyle(workbook={})", workbook);
        CellStyle style = workbook.createCellStyle();
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();
        style.setBorderRight(thin);
        style.setRightBorderColor(black);
        style.setBorderBottom(thin);
        style.setBottomBorderColor(black);
        style.setBorderLeft(thin);
        style.setLeftBorderColor(black);
        style.setBorderTop(thin);
        style.setTopBorderColor(black);
        return style;
    }

    public static Font createFont(Workbook workbook, CellSettings cellSettings) {
        LOGGER.trace("createFont(workbook={}, cellSettings={})", workbook, cellSettings);
        Font font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName(cellSettings.getFontName());
        font.setFontHeightInPoints(cellSettings.getFontHeight());
        font.setBold(cellSettings.isFontBold());
        return font;
    }

    public static short createDataFormat(Workbook workbook, String format) {
        LOGGER.trace("createDataFormat(workbook={}, format={})", workbook, format);
        CreationHelper creationHelper = workbook.getCreationHelper();
        return creationHelper.createDataFormat().getFormat(format);
    }

    public static void setCellStyle(Cell cell, CellStyle style) {
        LOGGER.trace("setCellStyle(cell={}, style={})", cell, style);
        Optional.ofNullable(style).ifPresent(cell::setCellStyle);
    }

}
