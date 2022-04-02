package com.epam.brest.model;

import com.epam.brest.model.annotation.ExcludeFromSql;
import com.epam.brest.model.validator.constraint.DateRange;
import com.epam.brest.model.validator.constraint.DifferentDates;
import com.epam.brest.model.validator.order.FirstOrder;
import com.epam.brest.model.validator.order.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.GroupSequence;
import java.time.LocalDate;

/**
 *  Credit card data transfer object for filtering by expiration date range.
 */
@DateRange(groups = {FirstOrder.class})
@DifferentDates(groups = {SecondOrder.class})
@GroupSequence({CreditCardFilterDto.class, FirstOrder.class, SecondOrder.class})
@Schema(description = "Credit card data transfer object for filtering by expiration date range")
public class CreditCardFilterDto {

    /**
     *  Start date of the credit card expiration range.
     */
    @Schema(hidden = true)
    private LocalDate fromDate;

    /**
     *  End date of the credit card expiration range.
     */
    @Schema(hidden = true)
    private LocalDate toDate;

    /**
     *  Start date format of the credit card expiration range.
     */
    @ExcludeFromSql
    @Schema(example = "06/2022", description = "Start date of the credit card expiration range")
    private String valueFromDate;

    /**
     *  End date format of the credit card expiration range.
     */
    @ExcludeFromSql
    @Schema(example = "08/2023", description = "End date of the credit card expiration range")
    private String valueToDate;

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getValueFromDate() {
        return valueFromDate;
    }

    public void setValueFromDate(String valueFromDate) {
        this.valueFromDate = valueFromDate;
    }

    public String getValueToDate() {
        return valueToDate;
    }

    public void setValueToDate(String valueToDate) {
        this.valueToDate = valueToDate;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
               "fromDate=" + fromDate +
               ", toDate=" + toDate +
               ", valueFromDate='" + valueFromDate + '\'' +
               ", valueToDate='" + valueToDate + '\'' +
               '}';
    }

}
