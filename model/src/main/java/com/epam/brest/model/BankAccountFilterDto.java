package com.epam.brest.model;

import com.epam.brest.model.annotation.SqlColumn;
import com.epam.brest.model.annotation.SqlRegexp;
import com.epam.brest.model.validator.constraint.AccountNumberPattern;
import com.epam.brest.model.validator.constraint.AnyPattern;
import com.epam.brest.model.validator.constraint.CustomerPattern;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *  Bank account data transfer object for filtering by account number and customer full name.
 */
@AnyPattern
@Schema(description = "Bank account data transfer object for filtering by account number and customer full name")
public class BankAccountFilterDto {

    /**
     *  Bank account number search pattern.
     */
    @SqlRegexp
    @AccountNumberPattern
    @SqlColumn("number")
    @Schema(example = "BY 99T6", description = "Bank account number search pattern")
    private String numberPattern;

    /**
     *  Bank customer full name search pattern.
     */
    @SqlRegexp
    @CustomerPattern
    @SqlColumn("customer")
    @Schema(example = "an ov", description = "Bank customer full name search pattern")
    private String customerPattern;

    public String getNumberPattern() {
        return numberPattern;
    }

    public void setNumberPattern(String numberPattern) {
        this.numberPattern = numberPattern;
    }

    public String getCustomerPattern() {
        return customerPattern;
    }

    public void setCustomerPattern(String customerPattern) {
        this.customerPattern = customerPattern;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
               "number='" + numberPattern + '\'' +
               ", customer='" + customerPattern + '\'' +
               '}';
    }

}
