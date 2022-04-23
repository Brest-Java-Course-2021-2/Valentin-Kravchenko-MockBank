package com.epam.brest.excel.api;

import com.epam.brest.model.BasicEntity;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface ExcelService<T extends BasicEntity> {

    Workbook buildWorkbook(List<T> data);

}
