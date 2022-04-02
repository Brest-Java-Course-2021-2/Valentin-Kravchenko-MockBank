package com.epam.brest.model;

import com.epam.brest.model.validator.constraint.CardNumbers;
import com.epam.brest.model.validator.constraint.DifferentCardNumbers;
import com.epam.brest.model.validator.constraint.SumOfMoney;
import com.epam.brest.model.validator.order.FirstOrder;
import com.epam.brest.model.validator.order.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.GroupSequence;
import java.math.BigDecimal;
import java.util.Locale;

/**
 *  Credit card transaction data transfer object.
 */
@CardNumbers(groups = {FirstOrder.class})
@DifferentCardNumbers(groups = {SecondOrder.class})
@SumOfMoney
@GroupSequence({CreditCardTransactionDto.class, FirstOrder.class, SecondOrder.class})
public class CreditCardTransactionDto {

    /**
     *  Number of a target credit card.
     */
    @Schema(example = "4000003394112581", description = "Number of a target credit card")
    private String targetCardNumber;

    /**
     *  Deposit sum of money.
     */
    @Schema(hidden = true)
    private BigDecimal sumOfMoney;

    /**
     *  Number of a source credit card.
     */
    @Schema(example = "4000002538269224", description = "Number of a source credit card")
    private String sourceCardNumber;

    /**
     *  Value of a sum of money from a http request.
     */
    @Schema(example = "1000,00", description = "Value of a sum of money")
    private String valueSumOfMoney;

    /**
     *  Current locale from a http request.
     */
    private Locale locale;

    public String getTargetCardNumber() {
        return targetCardNumber;
    }

    public void setTargetCardNumber(String targetCardNumber) {
        this.targetCardNumber = targetCardNumber;
    }

    public BigDecimal getSumOfMoney() {
        return sumOfMoney;
    }

    public void setSumOfMoney(BigDecimal sumOfMoney) {
        this.sumOfMoney = sumOfMoney;
    }

    public String getSourceCardNumber() {
        return sourceCardNumber;
    }

    public void setSourceCardNumber(String sourceCardNumber) {
        this.sourceCardNumber = sourceCardNumber;
    }

    public String getValueSumOfMoney() {
        return valueSumOfMoney;
    }

    public void setValueSumOfMoney(String valueSumOfMoney) {
        this.valueSumOfMoney = valueSumOfMoney;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
               "targetCardNumber='" + targetCardNumber + '\'' +
               ", sumOfMoney=" + sumOfMoney +
               ", sourceCardNumber='" + sourceCardNumber + '\'' +
               ", valueSumOfMoney='" + valueSumOfMoney + '\'' +
               ", locale=" + locale +
               '}';
    }

}
