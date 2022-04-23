package com.epam.brest.excel.impl;

import com.epam.brest.excel.api.ExcelService;
import com.epam.brest.excel.config.ExcelSettings;
import com.epam.brest.excel.util.ExcelServiceUtils;
import com.epam.brest.model.BankAccountDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class BankAccountDtoExcelServiceImpl implements ExcelService<BankAccountDto> {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoExcelServiceImpl.class);

    public static final int HEADER_ROW_IDX = 0;

    private final ExcelSettings excelSettings;

    public BankAccountDtoExcelServiceImpl(ExcelSettings accountsExcelSettings) {
        this.excelSettings = accountsExcelSettings;
    }

    @Override
    public Workbook createWorkbook(List<BankAccountDto> data) {
        LOGGER.debug("buildWorkbook(data={})", data);
        Workbook workbook = new XSSFWorkbook();
        Map<Class<?>, CellStyle> headerStyles = createHeaderStyles(workbook);
        Map<Class<?>, CellStyle> contentStyles = createContentStyles(workbook);
        Sheet sheet = ExcelServiceUtils.createSheet(workbook, excelSettings);
        Row headerRow = sheet.createRow(HEADER_ROW_IDX);
        ExcelServiceUtils.fillInRow(headerRow, excelSettings.getHeaderCells(), headerStyles);
        IntStream.rangeClosed(1, data.size())
                 .forEach(i -> {
                        Row contentRow = sheet.createRow(i);
                        Object[] values = extractValues(data.get(i - 1));
                        ExcelServiceUtils.fillInRow(contentRow, values, contentStyles);
        });
        return workbook;
    }

    @Override
    public ByteArrayResource export(Workbook workbook) {
        LOGGER.debug("export(workbook={})", workbook);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return new ByteArrayResource(out.toByteArray());
        } catch (IOException e) {
            LOGGER.error("export[IOException]", e);
            throw new RuntimeException(e);
        }
    }

    private Object[] extractValues(BankAccountDto bankAccountDto) {
        return new Object[]{bankAccountDto.getNumber(),
                            bankAccountDto.getCustomer(),
                            bankAccountDto.getRegistrationDate(),
                            bankAccountDto.getTotalCards()};
    }

    private Map<Class<?>, CellStyle> createHeaderStyles(Workbook workbook) {
        CellStyle generalStyle = ExcelServiceUtils.createCellStyle(workbook, excelSettings.getHeaderSettings());
        return Map.of(
                String.class, generalStyle
        );
    }

    private Map<Class<?>, CellStyle> createContentStyles(Workbook workbook) {
        CellStyle generalStyle = ExcelServiceUtils.createCellStyle(workbook, excelSettings.getContentSettings());
        CellStyle dateStyle = ExcelServiceUtils.createCellStyle(workbook, excelSettings.getContentSettings());
        dateStyle.setDataFormat(ExcelServiceUtils.createDataFormat(workbook, excelSettings.getDateFormat()));
        CellStyle intStyle = ExcelServiceUtils.createCellStyle(workbook, excelSettings.getContentSettings());
        intStyle.setDataFormat(ExcelServiceUtils.createDataFormat(workbook, excelSettings.getIntFormat()));
        return Map.of(
                String.class, generalStyle,
                LocalDate.class, dateStyle,
                Integer.class, intStyle
        );
    }

}
