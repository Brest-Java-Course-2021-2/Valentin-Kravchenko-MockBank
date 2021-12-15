package com.epam.brest.model.dto;

import com.epam.brest.model.annotation.ExcludeFromSql;
import com.epam.brest.model.validator.constraint.DateRange;
import com.epam.brest.model.validator.constraint.DifferentDates;
import com.epam.brest.model.validator.order.FirstOrder;
import com.epam.brest.model.validator.order.SecondOrder;

import javax.validation.GroupSequence;
import java.time.LocalDate;

/**
 *  Credit card data transfer object
 *  to filter by date range.
 */
@DateRange(groups = {FirstOrder.class})
@DifferentDates(groups = {SecondOrder.class})
@GroupSequence({CreditCardDateRangeDto.class, FirstOrder.class, SecondOrder.class})
public class CreditCardDateRangeDto {

    /**
     *  Start date of the range for credit card expiration.
     */
    private LocalDate fromDate;

    /**
     *  End date of the range for credit card expiration.
     */
    private LocalDate toDate;

    /**
     *  Value of the start date of the range from http request.
     */
    @ExcludeFromSql
    private String valueFromDate;

    /**
     *  Value of the end date of the range from http request.
     */
    @ExcludeFromSql
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
