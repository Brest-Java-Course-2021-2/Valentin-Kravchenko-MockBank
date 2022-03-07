package com.epam.brest.model.dto;

import com.epam.brest.model.annotation.SqlColumn;
import com.epam.brest.model.annotation.SqlRegexp;
import com.epam.brest.model.validator.constraint.AccountNumberPattern;
import com.epam.brest.model.validator.constraint.AnyPattern;
import com.epam.brest.model.validator.constraint.CustomerPattern;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *  Bank account data transfer object for filtering.
 */
@AnyPattern
public class BankAccountFilterDto {

    /**
     *  Search pattern for an international bank account number.
     */
    @SqlRegexp
    @AccountNumberPattern
    @SqlColumn("number")
    @Schema(example = "BY 99T6")
    private String numberPattern;

    /**
     *  Search pattern for a full name of the bank customer.
     */
    @SqlRegexp
    @CustomerPattern
    @SqlColumn("customer")
    @Schema(example = "an ov")
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
