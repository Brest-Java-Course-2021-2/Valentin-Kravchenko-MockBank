package com.epam.brest.model.dto;

/**
 *  Credit card data transfer object for transferring money.
 */
public class CreditCardTransferDto extends CreditCardDepositDto {

    /**
     *  Number of the source credit card.
     */
    private String sourceCardNumber;

    public String getSourceCardNumber() {
        return sourceCardNumber;
    }

    public void setSourceCardNumber(String sourceCardNumber) {
        this.sourceCardNumber = sourceCardNumber;
    }

}
