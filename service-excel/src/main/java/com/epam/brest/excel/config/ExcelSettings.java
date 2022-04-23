package com.epam.brest.excel.config;

public class ExcelSettings {

    private String sheet;

    private String[] headerCells;

    private String dateFormat;

    private String intFormat;

    private CellSettings headerSettings;

    private CellSettings contentSettings;

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public String[] getHeaderCells() {
        return headerCells;
    }

    public void setHeaderCells(String[] headerCells) {
        this.headerCells = headerCells;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getIntFormat() {
        return intFormat;
    }

    public void setIntFormat(String intFormat) {
        this.intFormat = intFormat;
    }

    public CellSettings getHeaderSettings() {
        return headerSettings;
    }

    public void setHeaderSettings(CellSettings headerSettings) {
        this.headerSettings = headerSettings;
    }

    public CellSettings getContentSettings() {
        return contentSettings;
    }

    public void setContentSettings(CellSettings contentSettings) {
        this.contentSettings = contentSettings;
    }

}
