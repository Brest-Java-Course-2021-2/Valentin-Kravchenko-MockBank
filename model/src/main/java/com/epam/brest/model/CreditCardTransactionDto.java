package com.epam.brest.model;

import com.epam.brest.model.validator.constraint.CreditCard;
import com.epam.brest.model.validator.constraint.DifferentCards;
import com.epam.brest.model.validator.constraint.TransactionAmount;
import com.epam.brest.model.validator.order.FirstOrder;
import com.epam.brest.model.validator.order.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.GroupSequence;
import java.util.Locale;

import static com.epam.brest.model.validator.constant.CreditCardTransactionType.SOURCE;
import static com.epam.brest.model.validator.constant.CreditCardTransactionType.TARGET;

/**
 *  Credit card transaction data transfer object.
 */
@DifferentCards(groups = {SecondOrder.class})
@TransactionAmount
@GroupSequence({CreditCardTransactionDto.class, FirstOrder.class, SecondOrder.class})
public class CreditCardTransactionDto {

    /**
     *  Number of a target credit card.
     */
    @Schema(example = "4000003394112581", description = "Number of a target credit card")
    @CreditCard(value = TARGET, groups = {FirstOrder.class})
    private String targetCardNumber;

    /**
     *  Number of a source credit card.
     */
    @Schema(example = "4000002538269224", description = "Number of a source credit card")
    @CreditCard(value = SOURCE, groups = {FirstOrder.class})
    private String sourceCardNumber;

    /**
     *  String representation of a transaction amount.
     */
    @Schema(example = "1000,00", description = "String representation of a transaction amount")
    private String transactionAmountValue;

    /**
     *  Current locale.
     */
    private Locale locale;

    public String getTargetCardNumber() {
        return targetCardNumber;
    }

    public void setTargetCardNumber(String targetCardNumber) {
        this.targetCardNumber = targetCardNumber;
    }

    public String getSourceCardNumber() {
        return sourceCardNumber;
    }

    public void setSourceCardNumber(String sourceCardNumber) {
        this.sourceCardNumber = sourceCardNumber;
    }

    public String getTransactionAmountValue() {
        return transactionAmountValue;
    }

    public void setTransactionAmountValue(String transactionAmountValue) {
        this.transactionAmountValue = transactionAmountValue;
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
               ", sourceCardNumber='" + sourceCardNumber + '\'' +
               ", transactionAmountValue='" + transactionAmountValue + '\'' +
               ", locale=" + locale +
               '}';
    }

}
