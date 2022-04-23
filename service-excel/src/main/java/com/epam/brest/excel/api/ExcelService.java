package com.epam.brest.excel.api;

import com.epam.brest.model.BasicEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;

public interface ExcelService<T extends BasicEntity> {

    Workbook createWorkbook(List<T> data);

    ByteArrayResource export(Workbook workbook);

}
