package com.epam.brest.model;

import com.epam.brest.model.annotation.MapToColumn;
import com.epam.brest.model.annotation.ConvertToRegexp;
import com.epam.brest.model.validator.constraint.AccountNumberPattern;
import com.epam.brest.model.validator.constraint.AnyOfPatterns;
import com.epam.brest.model.validator.constraint.CustomerPattern;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *  Bank account data transfer object for filtering by account number and customer full name.
 */
@AnyOfPatterns
@Schema(description = "Bank account data transfer object for filtering by account number and customer full name")
public class BankAccountFilterDto {

    /**
     *  Bank account number search pattern.
     */
    @ConvertToRegexp
    @MapToColumn("number")
    @AccountNumberPattern
    @Schema(example = "BY 99T6", description = "Bank account number search pattern")
    private String numberPattern;

    /**
     *  Bank customer search pattern.
     */
    @ConvertToRegexp
    @MapToColumn("customer")
    @CustomerPattern
    @Schema(example = "an ov", description = "Bank customer search pattern")
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
