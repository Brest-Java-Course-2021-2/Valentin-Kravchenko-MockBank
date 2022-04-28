package com.epam.brest.excel.impl;

import com.epam.brest.excel.api.ExcelService;
import com.epam.brest.excel.config.ExcelTestConfig;
import com.epam.brest.faker.api.FakerService;
import com.epam.brest.model.BankAccountDto;
import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ExcelTestConfig.class},
                properties = {"spring.output.ansi.enabled=always"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BankAccountDtoExcelServiceImplIT {

    public static final int TEST_DATA_AMOUNT = 10;
    public static final String PATH_TEMPLATE = "src/test/resources/%s.xlsx";

    private final ExcelService<BankAccountDto> excelService;

    private final List<BankAccountDto> fakeData;

    private String fileName;

    BankAccountDtoExcelServiceImplIT(FakerService<BankAccountDto> fakerService,
                                     ExcelService<BankAccountDto> excelService) {
        this.fakeData = fakerService.getFakeData(Optional.of(TEST_DATA_AMOUNT));
        this.excelService = excelService;
    }

    @BeforeEach
    public void generateExcelFile() throws IOException {
        Workbook workbook = excelService.createWorkbook(fakeData);
        fileName = workbook.getSheetName(0);
        Path path = Path.of(format(PATH_TEMPLATE, fileName));
        FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
        workbook.write(fileOutputStream);
        workbook.close();
    }

    @Test
    void export() throws IOException {
        Map<Integer, List<String>> dataFromExcel;
        try(FileInputStream file = new FileInputStream(format(PATH_TEMPLATE, fileName))) {
            dataFromExcel = parseWorkbook(new XSSFWorkbook(file));
        }
        assertEquals(fakeData.size(), dataFromExcel.size() - 1);
        List<String> customers = fakeData.stream()
                                         .map(BankAccountDto::getCustomer)
                                         .collect(toList());
        List<String> customersFromExcel = dataFromExcel.values().stream()
                                                                .skip(1)
                                                                .map(list -> list.get(1))
                                                                .collect(toList());
        assertEquals(customers, customersFromExcel);
    }

    private Map<Integer, List<String>> parseWorkbook(Workbook workbook) {
        Map<Integer, List<String>> data = new HashMap<>();
        CellDateFormatter cellDateFormatter = new CellDateFormatter("yyyy-mm-dd");
        Sheet sheet = workbook.getSheetAt(0);
        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<>());
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        data.get(i).add(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            data.get(i).add(cellDateFormatter.format(cell.getDateCellValue()) + "");
                        } else {
                            data.get(i).add((int) cell.getNumericCellValue() + "");
                        }
                        break;
                    default:
                        data.get(i).add(" ");
                }
            }
            i++;
        }
        return data;
    }

}