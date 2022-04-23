package com.epam.brest.excel.config;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

public class CellSettings {

    private String fontName;

    private Short fontHeight;

    private Boolean isFontBold;

    HorizontalAlignment alignment;

    IndexedColors foregroundColor;

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Short getFontHeight() {
        return fontHeight;
    }

    public void setFontHeight(Short fontHeight) {
        this.fontHeight = fontHeight;
    }

    public Boolean isFontBold() {
        return isFontBold;
    }

    public void setIsFontBold(Boolean isFontBold) {
        this.isFontBold = isFontBold;
    }

    public HorizontalAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(HorizontalAlignment alignment) {
        this.alignment = alignment;
    }

    public IndexedColors getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(IndexedColors foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

}
