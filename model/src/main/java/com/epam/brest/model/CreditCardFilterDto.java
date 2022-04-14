package com.epam.brest.model;

import com.epam.brest.model.annotation.ExcludeFromSql;
import com.epam.brest.model.validator.constraint.Range;
import com.epam.brest.model.validator.constraint.RangeDate;
import com.epam.brest.model.validator.order.FirstOrder;
import com.epam.brest.model.validator.order.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.GroupSequence;
import java.time.LocalDate;

import static com.epam.brest.model.validator.constant.RangeDateType.END;
import static com.epam.brest.model.validator.constant.RangeDateType.START;

/**
 *  Credit card data transfer object for filtering by expiration date range.
 */
@Range(groups = {SecondOrder.class})
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
     *  String representation of the start date of the credit card expiration range.
     */
    @ExcludeFromSql
    @RangeDate(value = START, groups = {FirstOrder.class})
    @Schema(example = "06/2022",
            description = "String representation of the start date of the credit card expiration range.")
    private String fromDateValue;

    /**
     *  String representation of the end date of the credit card expiration range.
     */
    @ExcludeFromSql
    @RangeDate(value = END, groups = {FirstOrder.class})
    @Schema(example = "08/2023",
            description = "String representation of the end date of the credit card expiration range")
    private String toDateValue;

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

    public String getFromDateValue() {
        return fromDateValue;
    }

    public void setFromDateValue(String fromDateValue) {
        this.fromDateValue = fromDateValue;
    }

    public String getToDateValue() {
        return toDateValue;
    }

    public void setToDateValue(String toDateValue) {
        this.toDateValue = toDateValue;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
               "fromDate=" + fromDate +
               ", toDate=" + toDate +
               ", fromDateValue='" + fromDateValue + '\'' +
               ", toDateValue='" + toDateValue + '\'' +
               '}';
    }

}
