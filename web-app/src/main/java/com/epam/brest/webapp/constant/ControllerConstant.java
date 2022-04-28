package com.epam.brest.webapp.constant;

public class ControllerConstant {

    public static final String REDIRECT_ACCOUNTS = "redirect:/accounts";
    public static final String REDIRECT_CARDS = "redirect:/cards";
    public static final String ACCOUNTS = "accounts";
    public static final String ACCOUNT = "account";
    public static final String CARD = "card";
    public static final String FILTER = "filter";
    public static final String CARDS = "cards";
    public static final String TRANSACTION = "transaction";
    public static final String MESSAGE = "message";
    public static final String ERROR = "error";
    public static final String CUSTOMER = "customer";

    public static final String NUMBER_PATTERN = "numberPattern";
    public static final String CUSTOMER_PATTERN = "customerPattern";
    public static final String SOURCE_CARD_NUMBER = "sourceCardNumber";
    public static final String TARGET_CARD_NUMBER = "targetCardNumber";
    public static final String TRANSACTION_AMOUNT = "transactionAmount";
    public static final String FROM_DATE_VALUE = "fromDateValue";
    public static final String TO_DATE_VALUE = "toDateValue";
    public static final String CUSTOMER_ERROR_CODE = "Customer.account.customer";
    public static final String CUSTOMER_PATTERN_ERROR_CODE = "CustomerPattern.account.dto.customerPattern";
    public static final String NUMBER_PATTERN_ERROR_CODE = "AccountNumberPattern.account.dto.numberPattern";
    public static final String TARGET_CARD_NUMBER_ERROR_CODE = "CardNumber.card.dto.targetCardNumber";
    public static final String SOURCE_CARD_NUMBER_ERROR_CODE = "CardNumber.card.dto.sourceCardNumber";
    public static final String DIFFERENT_CARD_NUMBERS_ERROR_CODE = "DifferentCardNumbers.card.dto.targetCardNumber";
    public static final String TRANSACTION_AMOUNT_ERROR_CODE = "TransactionAmount.card.dto.transactionAmount";
    public static final String FROM_DATE_VALUE_ERROR_CODE = "RangeDate.card.dto.fromDateValue";
    public static final String TO_DATE_VALUE_ERROR_CODE = "RangeDate.card.dto.toDateValue";
    public static final String FROM_DATE_ERROR_CODE = "Range.card.dto.fromDateValue";
    public static final String TO_DATE_ERROR_CODE = "Range.card.dto.toDateValue";

    public static final String JOIN_DELIMITER = ", ";
    public static final String JOIN_PREFIX = "[";
    public static final String JOIN_SUFFIX = "]";

}
